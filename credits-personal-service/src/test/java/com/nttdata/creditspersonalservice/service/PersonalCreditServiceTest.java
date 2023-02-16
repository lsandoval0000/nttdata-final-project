package com.nttdata.creditspersonalservice.service;

import com.nttdata.creditspersonalservice.controller.exception.*;
import com.nttdata.creditspersonalservice.dto.PersonalCreditDto;
import com.nttdata.creditspersonalservice.dto.PersonalCreditResponseDto;
import com.nttdata.creditspersonalservice.dto.mapper.NewPersonalCreditRequestDtoMapper;
import com.nttdata.creditspersonalservice.dto.mapper.PersonalCreditDtoMapper;
import com.nttdata.creditspersonalservice.dto.newcredit.NewPersonalCreditRequestDto;
import com.nttdata.creditspersonalservice.dto.payment.PaymentInfoDto;
import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import com.nttdata.creditspersonalservice.repository.PersonalCreditRepository;
import com.nttdata.creditspersonalservice.repository.TransactionRepository;
import com.nttdata.creditspersonalservice.service.externalapi.BankAccountSavingsAccountServiceClient;
import com.nttdata.creditspersonalservice.service.externalapi.dto.SavingsAccountResponseDto;
import com.nttdata.creditspersonalservice.service.implementation.PersonalCreditServiceImpl;
import com.nttdata.creditspersonalservice.util.ClientType;
import com.nttdata.creditspersonalservice.util.DefaultValues;
import com.nttdata.creditspersonalservice.util.PaymentOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalCreditServiceTest {

    @Mock
    private BankAccountSavingsAccountServiceClient bankAccountSavingsAccountServiceClient;
    private PersonalCreditDtoMapper personalCreditDtoMapper;
    private NewPersonalCreditRequestDtoMapper newPersonalCreditRequestDtoMapper;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private PersonalCreditRepository personalCreditRepository;
    private PersonalCreditServiceImpl underTest;

    @BeforeEach
    void setup() {
        personalCreditDtoMapper = new PersonalCreditDtoMapper();
        newPersonalCreditRequestDtoMapper = new NewPersonalCreditRequestDtoMapper();
        underTest = new PersonalCreditServiceImpl(
                bankAccountSavingsAccountServiceClient,
                personalCreditDtoMapper,
                newPersonalCreditRequestDtoMapper,
                personalCreditRepository,
                transactionRepository);
    }

    @Test
    @DisplayName("Debe permitir solicitar un crédito al cliente")
    void shouldLetTheClientRequestCredit() {
        NewPersonalCreditRequestDto creditRequest = NewPersonalCreditRequestDto
                .builder()
                .dni("47081541")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .build();
        PersonalCredit personalCreditEntity = PersonalCredit
                .builder()
                .dni(creditRequest.getDni())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .isActive(true)
                .build();
        PersonalCreditDto personalCreditDto = PersonalCreditDto
                .builder()
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .build();
        PersonalCreditResponseDto expected = PersonalCreditResponseDto
                .builder()
                .personalCredit(personalCreditDto)
                .message("Se ha aceptado su solicitud de crédito.")
                .build();

        given(personalCreditRepository.countActivePersonalCreditByDni(creditRequest.getDni())).willReturn(0L);
        given(personalCreditRepository.save(any())).willReturn(personalCreditEntity);

        PersonalCreditResponseDto response = underTest.requestCredit(creditRequest);

        assertEquals(expected.getMessage(), response.getMessage());
        assertEquals(expected.getPersonalCredit().getCreditAmount(), response.getPersonalCredit().getCreditAmount());
    }

    @Test
    @DisplayName("Debe generar la excepción UnsupportedClientTypeException si el tipo de cliente no es válido" +
            " al tratar de solicitar un crédito")
    void shouldRaiseUnsupportedClientTypeExceptionWhenClientTypeIsNotValid() {
        NewPersonalCreditRequestDto creditRequest = NewPersonalCreditRequestDto
                .builder()
                .clientType(ClientType.EMPRESARIAL.toString())
                .build();

        assertThrows(UnsupportedClientTypeException.class, () -> underTest.requestCredit(creditRequest));
        verify(personalCreditRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar la excepción MaxValueAllowedReachedException si el cliente ya posee un crédito activo" +
            " al tratar de solicitar un crédito")
    void shouldRaiseMaxValueAllowedReachedExceptionWhenClientAlreadyHasAnActiveCredit() {
        NewPersonalCreditRequestDto creditRequest = NewPersonalCreditRequestDto
                .builder()
                .dni("47081541")
                .clientType(ClientType.PERSONAL.toString())
                .build();

        given(personalCreditRepository.countActivePersonalCreditByDni(creditRequest.getDni())).willReturn(1L);

        assertThrows(MaxValueAllowedReachedException.class, () -> underTest.requestCredit(creditRequest));
        verify(personalCreditRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar la excepción MaxValueAllowedReachedException si el monto de crédito sobrepasa el límite" +
            " al tratar de solicitar un crédito")
    void shouldRaiseMaxValueAllowedReachedExceptionWhenCreditAmountIsGreaterThanCreditLimit() {
        NewPersonalCreditRequestDto creditRequest = NewPersonalCreditRequestDto
                .builder()
                .dni("47081541")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(BigDecimal.valueOf(50000000L))
                .build();

        given(personalCreditRepository.countActivePersonalCreditByDni(creditRequest.getDni())).willReturn(0L);

        assertThrows(MaxValueAllowedReachedException.class, () -> underTest.requestCredit(creditRequest));
        verify(personalCreditRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe permitir al cliente pagar parte del crédito y agregar un monto por mora")
    void shouldLetClientPayCreditAndAddAmountForLatePayment() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(100L))
                .paymentMethod(PaymentOption.CASH.toString())
                .build();
        PersonalCredit personalCreditEntity = PersonalCredit
                .builder()
                .creditId(1L)
                .dni(dni)
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().minus(10, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .isActive(true)
                .build();
        PersonalCreditDto personalCreditDto = new PersonalCreditDto();
        BeanUtils.copyProperties(personalCreditEntity, personalCreditDto);
        BigDecimal amountForLatePayment = personalCreditEntity
                .getMonthlyFee()
                .multiply(personalCreditEntity.getLatePaymentInterest().divide(BigDecimal.valueOf(100L)));
        personalCreditDto.setAmountLeft(personalCreditDto.getAmountLeft().add(amountForLatePayment));
        personalCreditDto.setAmountLeft(personalCreditDto.getAmountLeft().subtract(paymentInfo.getAmountToPay()));

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCreditEntity));
        given(personalCreditRepository.save(any())).willReturn(personalCreditEntity);

        PersonalCreditResponseDto response = underTest.payCredit(dni, paymentInfo);

        assertEquals(personalCreditDto.getAmountLeft(), response.getPersonalCredit().getAmountLeft());
        assertTrue(response.getMessage().contains("retraso"));
    }

    @Test
    @DisplayName("Debe permitir al cliente pagar el crédito por completo y debe darse por pagado el crédito")
    void shouldLetClientPayCreditAndFinishCreditWhenAllAmountIsPayed() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .paymentMethod(PaymentOption.CASH.toString())
                .build();
        PersonalCredit personalCreditEntity = PersonalCredit
                .builder()
                .creditId(1L)
                .dni(dni)
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .isActive(false)
                .build();
        PersonalCreditDto personalCreditDto = new PersonalCreditDto();
        BeanUtils.copyProperties(personalCreditEntity, personalCreditDto);

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCreditEntity));
        given(personalCreditRepository.save(any())).willReturn(personalCreditEntity);

        PersonalCreditResponseDto response = underTest.payCredit(dni, paymentInfo);

        assertEquals(BigDecimal.ZERO, response.getPersonalCredit().getAmountLeft());
        assertEquals(false, personalCreditEntity.getIsActive());
        assertTrue(response.getMessage().contains("Se ha pagado el monto total del crédito"));
    }

    @Test
    @DisplayName("Debe permitir al cliente pagar el crédito con dinero en efectivo")
    void shouldLetClientPayCreditWithCash() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(1500L))
                .paymentMethod(PaymentOption.CASH.toString())
                .build();
        PersonalCredit personalCreditEntity = PersonalCredit
                .builder()
                .creditId(1L)
                .dni(dni)
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .isActive(false)
                .build();
        PersonalCreditDto personalCreditDto = new PersonalCreditDto();
        BeanUtils.copyProperties(personalCreditEntity, personalCreditDto);

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCreditEntity));
        given(personalCreditRepository.save(any())).willReturn(personalCreditEntity);

        PersonalCreditResponseDto response = underTest.payCredit(dni, paymentInfo);

        assertEquals(personalCreditEntity.getCreditAmount().subtract(paymentInfo.getAmountToPay()),
                response.getPersonalCredit().getAmountLeft());
    }

    @Test
    @DisplayName("Debe permitir al cliente pagar el crédito empleando su cuenta de ahorros")
    void shouldLetClientPayCreditWithSavingsAccount() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(1500L))
                .paymentMethod(PaymentOption.SAVINGS_ACCOUNT.toString())
                .build();
        PersonalCredit personalCreditEntity = PersonalCredit
                .builder()
                .creditId(1L)
                .dni(dni)
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .isActive(false)
                .build();
        PersonalCreditDto personalCreditDto = new PersonalCreditDto();
        BeanUtils.copyProperties(personalCreditEntity, personalCreditDto);

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCreditEntity));
        given(bankAccountSavingsAccountServiceClient.payUsingAccount(any(), any()))
                .willReturn(SavingsAccountResponseDto.builder().build());
        given(personalCreditRepository.save(any())).willReturn(personalCreditEntity);

        PersonalCreditResponseDto response = underTest.payCredit(dni, paymentInfo);

        assertEquals(personalCreditEntity.getCreditAmount().subtract(paymentInfo.getAmountToPay()),
                response.getPersonalCredit().getAmountLeft());
    }

    @Test
    @DisplayName("Debe generar la excepción BankAccountSavingsAccountServiceException cuando ocurre un error" +
            " con el servicio de pago mediante cuenta de ahorro")
    void shouldRaiseBankAccountSavingsAccountServiceExceptionWhenPayingWithSavingsAccountAndGetsErrorFromService() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(1500L))
                .paymentMethod(PaymentOption.SAVINGS_ACCOUNT.toString())
                .build();
        PersonalCredit personalCreditEntity = PersonalCredit
                .builder()
                .creditId(1L)
                .dni(dni)
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .isActive(false)
                .build();
        PersonalCreditDto personalCreditDto = new PersonalCreditDto();
        BeanUtils.copyProperties(personalCreditEntity, personalCreditDto);

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCreditEntity));
        given(bankAccountSavingsAccountServiceClient.payUsingAccount(any(), any()))
                .willThrow(BankAccountSavingsAccountServiceException.class);

        assertThrows(BankAccountSavingsAccountServiceException.class, () -> underTest.payCredit(dni, paymentInfo));
        verify(personalCreditRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar la excepción NoSuchElementFoundException cuando el cliente trata de pagar un " +
            " crédito que no posee")
    void shouldRaiseNoSuchElementFoundExceptionWhenClientTryToPayANonExistingCredit() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto.builder().build();

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> underTest.payCredit(dni, paymentInfo));
        verify(personalCreditRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar la excepción InvalidValueProvidedException cuando el cliente indica un " +
            "método de pago inválido al pagar un crédito")
    void shouldRaiseInvalidValueProvidedExceptionWhenClientTryToPayCreditWithAnInvalidPaymentMethod() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.TEN)
                .paymentMethod("METODO_INVALIDO")
                .build();
        PersonalCredit personalCreditEntity = PersonalCredit
                .builder()
                .dni(dni)
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .isActive(true)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCreditEntity));

        assertThrows(InvalidValueProvidedException.class, () -> underTest.payCredit(dni, paymentInfo));
        verify(personalCreditRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar el balance del crédito activo del cliente")
    void shouldGetCreditBalanceIfClientHasActiveCredit() {
        String dni = "47081541";
        PersonalCredit personalCreditEntity = PersonalCredit.builder().creditAmount(BigDecimal.TEN).build();

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCreditEntity));

        PersonalCreditResponseDto creditBalance = underTest.getCreditBalance(dni);

        assertEquals(personalCreditEntity.getCreditAmount(), creditBalance.getPersonalCredit().getCreditAmount());
    }

    @Test
    @DisplayName("Debe generar la excepción NoSuchElementFoundException cuando el cliente no posee un crédito activo" +
            " al tratar de obtener el balance de un crédito")
    void shouldRaiseNoSuchElementFoundExceptionWhenClientDoesNotHaveAnActiveCreditAndTryToGetTheBalance() {
        String dni = "47081541";

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> underTest.getCreditBalance(dni));
    }
}