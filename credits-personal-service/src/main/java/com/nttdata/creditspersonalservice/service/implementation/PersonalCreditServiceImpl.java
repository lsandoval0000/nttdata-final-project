package com.nttdata.creditspersonalservice.service.implementation;

import com.nttdata.creditspersonalservice.controller.exception.*;
import com.nttdata.creditspersonalservice.dto.PersonalCreditResponseDto;
import com.nttdata.creditspersonalservice.dto.mapper.NewPersonalCreditRequestDtoMapper;
import com.nttdata.creditspersonalservice.dto.mapper.PersonalCreditDtoMapper;
import com.nttdata.creditspersonalservice.dto.newcredit.NewPersonalCreditRequestDto;
import com.nttdata.creditspersonalservice.dto.payment.PaymentInfoDto;
import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import com.nttdata.creditspersonalservice.entity.Transaction;
import com.nttdata.creditspersonalservice.repository.PersonalCreditRepository;
import com.nttdata.creditspersonalservice.repository.TransactionRepository;
import com.nttdata.creditspersonalservice.service.PersonalCreditService;
import com.nttdata.creditspersonalservice.service.externalapi.BankAccountSavingsAccountServiceClient;
import com.nttdata.creditspersonalservice.service.externalapi.dto.SavingsAccountPaymentInfoDto;
import com.nttdata.creditspersonalservice.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static com.nttdata.creditspersonalservice.util.PaymentOption.*;
import static java.util.Map.entry;

/**
 * The personal credit service.
 */
@Service
@RequiredArgsConstructor
public class PersonalCreditServiceImpl implements PersonalCreditService {

    private final BankAccountSavingsAccountServiceClient bankAccountSavingsAccountServiceClient;
    private final PersonalCreditDtoMapper personalCreditDtoMapper;
    private final NewPersonalCreditRequestDtoMapper newPersonalCreditRequestDtoMapper;
    private final PersonalCreditRepository personalCreditRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Request personal credit.
     *
     * @param newPersonalCreditRequestDto the new personal credit request dto
     * @return the personal credit response dto
     */
    @Transactional
    @Override
    public PersonalCreditResponseDto requestCredit(NewPersonalCreditRequestDto newPersonalCreditRequestDto) {
        if (newPersonalCreditRequestDto.getClientType().equals(ClientType.EMPRESARIAL.toString())) {
            throw new UnsupportedClientTypeException(
                    "Los tipos de cliente empresarial no pueden solicitar un crédito personal."
            );
        }

        if (personalCreditRepository.countActivePersonalCreditByDni(newPersonalCreditRequestDto.getDni()) > 0) {
            throw new MaxValueAllowedReachedException("Solo puede posee un crédito personal activo a la vez.");
        }

        if (newPersonalCreditRequestDto.getCreditAmount().compareTo(DefaultValues.PERSONAL_CREDIT_LIMIT) > 0) {
            throw new MaxValueAllowedReachedException(
                    "El monto de crédito excede el límite para los créditos personales.");
        }

        PersonalCredit personalCredit = newPersonalCreditRequestDtoMapper.convertToEntity(newPersonalCreditRequestDto);
        personalCredit.setAmountLeft(personalCredit.getCreditAmount());
        personalCredit.setIsActive(true);
        personalCredit = personalCreditRepository.save(personalCredit);
        Transaction transaction = Transaction
                .builder()
                .amount(personalCredit.getCreditAmount())
                .personalCredit(personalCredit)
                .description("Se ha autorizado un crédito personal para el cliente con DNI " + personalCredit.getDni())
                .build();
        transactionRepository.save(transaction);

        return PersonalCreditResponseDto
                .builder()
                .personalCredit(personalCreditDtoMapper.convertToDto(personalCredit))
                .message("Se ha aceptado su solicitud de crédito.")
                .build();
    }

    /**
     * Pay personal credit.
     *
     * @param dni         the dni
     * @param paymentInfo the payment info
     * @return the personal credit response dto
     */
    @Transactional
    @Override
    public PersonalCreditResponseDto payCredit(String dni, PaymentInfoDto paymentInfo) {
        Transaction transaction = Transaction.builder().build();

        PersonalCredit personalCredit = personalCreditRepository.findActiveCreditByDni(dni).orElse(null);

        if (personalCredit == null) {
            throw new NoSuchElementFoundException("El cliente no posee un crédito activo.");
        }

        String addToResponseMessage = calculatePayment(paymentInfo, transaction, personalCredit);

        transaction.setPersonalCredit(personalCredit);
        transaction.setDescription(paymentOption(paymentInfo, transaction, personalCredit));

        personalCredit = personalCreditRepository.save(personalCredit);
        transactionRepository.save(transaction);

        return PersonalCreditResponseDto
                .builder()
                .personalCredit(personalCreditDtoMapper.convertToDto(personalCredit))
                .message("Pago de crédito realizado exitosamente." + addToResponseMessage)
                .build();
    }

    private static String calculatePayment(
            PaymentInfoDto paymentInfo,
            Transaction transaction,
            PersonalCredit personalCredit) {
        String addToResponseMessage = "";
        OffsetDateTime currentDate = OffsetDateTime.now();
        OffsetDateTime payDay = personalCredit.getInitialDateToPay().toInstant().atOffset(ZoneOffset.UTC);
        BigDecimal amountForLatePayment = BigDecimal.ZERO;

        if (currentDate.getDayOfMonth() > payDay.getDayOfMonth()) {
            amountForLatePayment = personalCredit
                    .getMonthlyFee()
                    .multiply(personalCredit.getLatePaymentInterest());
            personalCredit.setAmountLeft(personalCredit
                    .getAmountLeft()
                    .add(amountForLatePayment));
        }

        BigDecimal amountLeftToPay = personalCredit.getAmountLeft().subtract(paymentInfo.getAmountToPay());

        if (amountLeftToPay.compareTo(BigDecimal.ZERO) <= 0) {
            transaction.setAmount(personalCredit.getAmountLeft());
            personalCredit.setAmountLeft(BigDecimal.ZERO);
            personalCredit.setIsActive(false);
            addToResponseMessage += "\nSe ha pagado el monto total del crédito.";
            if (amountLeftToPay.compareTo(BigDecimal.ZERO) < 0) {
                addToResponseMessage += "\nEl monto entregado excedía el mongo a pagar, se entrega "
                        + amountLeftToPay.negate().toString()
                        + " soles";
            }
        } else {
            personalCredit.setAmountLeft(amountLeftToPay);
            transaction.setAmount(paymentInfo.getAmountToPay());
        }

        if (amountForLatePayment.compareTo(BigDecimal.ZERO) != 0) {
            addToResponseMessage += "\nSe ha cobrado el monto de "
                    + amountForLatePayment.toString()
                    + " soles, por haber realizado el pago de cuota con retraso.";
        }

        return addToResponseMessage;
    }

    private String paymentOption(
            PaymentInfoDto paymentInfo,
            Transaction transaction,
            PersonalCredit personalCredit) {
        String transactionDesctiption = null;
        EnumValidator enumValidator = EnumValidator.of(PaymentOption.class);

        if (!enumValidator.isPresent(paymentInfo.getPaymentMethod())) {
            throw new InvalidValueProvidedException("El tipo de pago seleccionado no es válido.");
        }

        switch (valueOf(paymentInfo.getPaymentMethod())) {
            case CASH:
                transactionDesctiption = StrSubstitutor.replace(
                        StringTemplates.TRANSACTION_DESCRIPTION_TEMPLATE,
                        Map.ofEntries(
                                entry("dni", personalCredit.getDni()),
                                entry("accion", "realiza el pago del crédito, en efectivo, por un monto de"),
                                entry("monto", transaction.getAmount().toString()),
                                entry("pendiente", personalCredit.getAmountLeft().toString())
                        ));
                break;
            case SAVINGS_ACCOUNT:
                SavingsAccountPaymentInfoDto requestPay = SavingsAccountPaymentInfoDto
                        .builder()
                        .amountToPay(transaction.getAmount())
                        .serviceToPay("Pago de crédito personal.")
                        .build();
                bankAccountSavingsAccountServiceClient.payUsingAccount(personalCredit.getDni(), requestPay);
                transactionDesctiption = StrSubstitutor.replace(
                        StringTemplates.TRANSACTION_DESCRIPTION_TEMPLATE,
                        Map.ofEntries(
                                entry("dni", personalCredit.getDni()),
                                entry("accion", "realiza el pago del crédito, empleando su cuenta de ahorros, "
                                        + "por un monto de"),
                                entry("monto", transaction.getAmount().toString()),
                                entry("pendiente", personalCredit.getAmountLeft().toString())
                        ));
                break;
            default:
        }
        return transactionDesctiption;
    }

    /**
     * Gets credit balance.
     *
     * @param dni the dni
     * @return the credit balance
     */
    @Override
    public PersonalCreditResponseDto getCreditBalance(String dni) {
        PersonalCredit personalCredit = personalCreditRepository.findActiveCreditByDni(dni).orElse(null);
        if (personalCredit == null) {
            throw new NoSuchElementFoundException("El cliente no posee un crédito activo.");
        }
        return PersonalCreditResponseDto
                .builder()
                .personalCredit(personalCreditDtoMapper.convertToDto(personalCredit))
                .message("Consulta realizada exitosamente")
                .build();
    }
}
