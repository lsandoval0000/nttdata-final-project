package com.nttdata.bankaccountsavingsservice.service.implementation;

import com.nttdata.bankaccountsavingsservice.controller.exception.MaxValueAllowedReachedException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NotEnoughFundsException;
import com.nttdata.bankaccountsavingsservice.controller.exception.UnsupportedClientTypeException;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountDto;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.mapper.SavingsAccountDtoMapper;
import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.entity.Transaction;
import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import com.nttdata.bankaccountsavingsservice.repository.TransactionRepository;
import com.nttdata.bankaccountsavingsservice.repository.SavingsAccountRepository;
import com.nttdata.bankaccountsavingsservice.service.SavingsAccountService;
import com.nttdata.bankaccountsavingsservice.service.api.ClientServiceClient;
import com.nttdata.bankaccountsavingsservice.util.AccountNumberGenerator;
import com.nttdata.bankaccountsavingsservice.util.ClientType;
import com.nttdata.bankaccountsavingsservice.util.DefaultValues;
import com.nttdata.bankaccountsavingsservice.util.StringTemplates;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Map.entry;

/**
 * Savings account service.
 */
@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final TransactionRepository transactionRepository;
    private final ClientServiceClient clientServiceClient;
    private final SavingsAccountDtoMapper savingsAccountDtoMapper;

    /**
     * New savings account.
     *
     * @param newSavingsAccountRequestDto the new savings account request dto
     * @return the savings account dto
     */
    @Transactional
    @Override
    public SavingsAccountDto newSavingsAccount(NewSavingsAccountRequestDto newSavingsAccountRequestDto) {
        if (newSavingsAccountRequestDto.getClientType().equals(ClientType.EMPRESARIAL.toString())) {
            throw new UnsupportedClientTypeException(
                    "Los tipos de cliente empresarial no pueden crear cuentas de ahorro."
            );
        }

//        if (!clientServiceClient.clientExists(newSavingsAccountRequestDto.getDni())) {
//            throw new NoSuchElementFoundException("El cliente no existe.");
//        }

        if (savingsAccountRepository.countSavingsAccountByDni(newSavingsAccountRequestDto.getDni()) > 0) {
            throw new MaxValueAllowedReachedException("Solo se puede poseer una cuenta por cliente como máximo.");
        }

        SavingsAccount account = SavingsAccount
                .builder()
                .accountNumber(AccountNumberGenerator.generateAccountNumber())
                .dni(newSavingsAccountRequestDto.getDni())
                .balance(newSavingsAccountRequestDto.getInitialAmount())
                .maintenanceFee(DefaultValues.MAINTENANCE_FEE)
                .monthlyAvailableMovements(DefaultValues.MONTHLY_AVAILABLE_MOVEMENTS)
                .build();
        SavingsAccount savedAccount = savingsAccountRepository.save(account);
        Transaction transaction = Transaction
                .builder()
                .amount(savedAccount.getBalance())
                .savingsAccount(savedAccount)
                .description("Se crea cuenta de ahorro para el cliente con DNI " + savedAccount.getDni())
                .build();
        transactionRepository.save(transaction);

        return savingsAccountDtoMapper.convertToDto(savedAccount);
    }

    /**
     * Deposit money into savings account.
     *
     * @param dni                    the dni
     * @param depositMoneyRequestDto the deposit money request dto
     * @return the savings account response dto
     */
    @Transactional
    @Override
    public SavingsAccountResponseDto depositMoneyIntoAccount(
            String dni,
            DepositMoneyRequestDto depositMoneyRequestDto) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);

        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }

        BigDecimal newBalance = savingsAccount.getBalance().add(depositMoneyRequestDto.getAmount());
        savingsAccount.setBalance(newBalance);
        SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);
        Transaction transaction = Transaction
                .builder()
                .amount(savedAccount.getBalance())
                .savingsAccount(savedAccount)
                .description(
                        StrSubstitutor.replace(
                                StringTemplates.TRANSACTION_DESCRIPTION_TEMPLATE,
                                Map.ofEntries(
                                        entry("dni", savedAccount.getDni()),
                                        entry("accion", "deposita"),
                                        entry("monto", depositMoneyRequestDto.getAmount().toString()),
                                        entry("balance", savedAccount.getBalance().toString())
                                )))
                .build();
        transactionRepository.save(transaction);

        return SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDtoMapper.convertToDto(savedAccount))
                .message("Depósito realizado exitosamente")
                .build();
    }

    /**
     * Withdraw money from savings account.
     *
     * @param dni                     the dni
     * @param withdrawMoneyRequestDto the withdrawal money request dto
     * @return the savings account response dto
     */
    @Transactional
    @Override
    public SavingsAccountResponseDto withdrawMoneyFromAccount(
            String dni,
            WithdrawMoneyRequestDto withdrawMoneyRequestDto) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);

        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }

        BigDecimal newBalance = savingsAccount.getBalance().subtract(withdrawMoneyRequestDto.getAmount());
        Integer transactionsOfCurrentMonth = transactionRepository.countAllTransactionsOfCurrentMonth();

        if (savingsAccount.getMonthlyAvailableMovements() - transactionsOfCurrentMonth <= 0) {
            newBalance = newBalance.subtract(DefaultValues.FEE_PER_OPERATION);
            savingsAccount.setMonthlyAvailableMovements(0);
        } else {
            savingsAccount.setMonthlyAvailableMovements(savingsAccount.getMonthlyAvailableMovements() - 1);
        }

        if (newBalance.compareTo(BigDecimal.valueOf(0L)) < 0) {
            throw new NotEnoughFundsException(
                    "El monto que el cliente desea retirar excede sus ahorros."
                            + (
                            (savingsAccount.getMonthlyAvailableMovements().equals(0)) ?
                                    "\n Puede deberse al cobro de "
                                            + DefaultValues.FEE_PER_OPERATION.toString()
                                            + " soles de comisión por operación." :
                                    ""
                    )
            );
        }

        savingsAccount.setBalance(newBalance);
        SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);
        Transaction transaction = Transaction
                .builder()
                .amount(savedAccount.getBalance())
                .savingsAccount(savedAccount)
                .description(
                        StrSubstitutor.replace(
                                StringTemplates.TRANSACTION_DESCRIPTION_TEMPLATE,
                                Map.ofEntries(
                                        entry("dni", savedAccount.getDni()),
                                        entry("accion", "retira"),
                                        entry("monto", withdrawMoneyRequestDto.getAmount().toString()),
                                        entry("balance", savedAccount.getBalance().toString())
                                )))
                .build();
        transactionRepository.save(transaction);

        return SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDtoMapper.convertToDto(savedAccount))
                .message("Retiro realizado exitosamente")
                .build();
    }

    /**
     * Pay using savings account.
     *
     * @param dni         the dni
     * @param paymentInfo the payment info
     * @return the savings account response dto
     */
    @Transactional
    @Override
    public SavingsAccountResponseDto payUsingAccount(String dni, PaymentInfoDto paymentInfo) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);

        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }

        BigDecimal newBalance = savingsAccount.getBalance().subtract(paymentInfo.getAmountToPay());

        if (newBalance.compareTo(BigDecimal.valueOf(0L)) < 0) {
            throw new NotEnoughFundsException("El monto que el cliente desea pagar excede sus ahorros.");
        }

        savingsAccount.setBalance(newBalance);
        SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);
        Transaction transaction = Transaction
                .builder()
                .amount(savedAccount.getBalance())
                .savingsAccount(savedAccount)
                .description(
                        StrSubstitutor.replace(
                                StringTemplates.TRANSACTION_DESCRIPTION_TEMPLATE,
                                Map.ofEntries(
                                        entry("dni", savedAccount.getDni()),
                                        entry("accion", "realiza el pago del servicio '"
                                                + paymentInfo.getServiceToPay()
                                                + "' por un monto de"),
                                        entry("monto", paymentInfo.getAmountToPay().toString()),
                                        entry("balance", savedAccount.getBalance().toString())
                                )))
                .build();
        transactionRepository.save(transaction);

        return SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDtoMapper.convertToDto(savedAccount))
                .message("Pago de servicio realizado exitosamente")
                .build();
    }

    /**
     * Gets account balance.
     *
     * @param dni the dni
     * @return the account balance
     */
    @Override
    public SavingsAccountResponseDto getAccountBalance(String dni) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);
        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }
        return SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDtoMapper.convertToDto(savingsAccount))
                .message("Consulta realizada exitosamente")
                .build();
    }
}
