package com.nttdata.bankaccountsavingsservice.controller.validation;

import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DepositMoneyRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Monto de depósito debe ser mayor a cero")
    void amountMustBeGreaterThanZero() {
        DepositMoneyRequestDto request = DepositMoneyRequestDto
                .builder()
                .amount(BigDecimal.valueOf(-2L))
                .build();

        Set<ConstraintViolation<DepositMoneyRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
