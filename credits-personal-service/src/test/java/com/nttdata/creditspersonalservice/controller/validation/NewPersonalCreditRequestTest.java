package com.nttdata.creditspersonalservice.controller.validation;

import com.nttdata.creditspersonalservice.dto.newcredit.NewPersonalCreditRequestDto;
import com.nttdata.creditspersonalservice.util.ClientType;
import com.nttdata.creditspersonalservice.util.DefaultValues;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NewPersonalCreditRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("El dni no debe ser nulo")
    void dniMustNotBeNull() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni(null)
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El tipo de cliente no debe ser nulo")
    void clientTypeMustNotBeNull() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni("12345678")
                .clientType(null)
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El monto del crédito no debe ser menor o igual a cero")
    void creditAmountMustBeGreaterThanZero() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni("12345678")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(BigDecimal.ZERO)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("La fecha de inicio de pago debe ser dias posteriores al resgistro del crédito")
    void initialDateToPayMustBeInFuture() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni("12345678")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El número de meses de pago debe ser mayor a cero")
    void monthsToPayMustBeGreaterThanZero() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni("12345678")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(0)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("La cuota mensual de pago debe ser mayor a cero")
    void monthlyFeeMustBeGreaterThanZero() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni("12345678")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(10)
                .monthlyFee(BigDecimal.ZERO)
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El interés debe ser mayor a cero")
    void interestRateMustBeGreaterThanZero() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni("12345678")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(10)
                .monthlyFee(BigDecimal.valueOf(1400L))
                .interestRate(BigDecimal.ZERO)
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El interés por mora debe ser mayor a cero")
    void latePaymentInterestMustBeGreaterThanZero() {
        NewPersonalCreditRequestDto request = NewPersonalCreditRequestDto
                .builder()
                .dni("12345678")
                .clientType(ClientType.PERSONAL.toString())
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(10)
                .monthlyFee(BigDecimal.valueOf(1400L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.ZERO)
                .build();

        Set<ConstraintViolation<NewPersonalCreditRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
