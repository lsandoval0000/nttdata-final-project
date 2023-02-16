package com.nttdata.bankaccountsavingsservice.controller.validation;

import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NewSavingsAccountRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("El dni no debe ser nulo")
    void dniMustNotBeNull() {
        NewSavingsAccountRequestDto request = NewSavingsAccountRequestDto
                .builder()
                .clientType("TEST")
                .initialAmount(BigDecimal.valueOf(0L))
                .build();

        Set<ConstraintViolation<NewSavingsAccountRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El tipo de cliente no debe ser nulo")
    void clientTypeMustNotBeNull() {
        NewSavingsAccountRequestDto request = NewSavingsAccountRequestDto
                .builder()
                .dni("12345678")
                .initialAmount(BigDecimal.valueOf(0L))
                .build();

        Set<ConstraintViolation<NewSavingsAccountRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El monto inicial debe ser mayor o igual a cero")
    void initialAmountMustNotBeNullAndGreaterThanZero() {
        NewSavingsAccountRequestDto request = NewSavingsAccountRequestDto
                .builder()
                .dni("12345678")
                .clientType("TEST")
                .initialAmount(BigDecimal.valueOf(-2L))
                .build();

        Set<ConstraintViolation<NewSavingsAccountRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
