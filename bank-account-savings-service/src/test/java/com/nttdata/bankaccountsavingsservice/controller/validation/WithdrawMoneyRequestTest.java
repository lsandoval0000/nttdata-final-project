package com.nttdata.bankaccountsavingsservice.controller.validation;

import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class WithdrawMoneyRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("El monto de retiro debe ser mayor a cero")
    void amountMustBeGreaterThanZero() {
        WithdrawMoneyRequestDto request = WithdrawMoneyRequestDto
                .builder()
                .amount(BigDecimal.valueOf(-2L))
                .build();

        Set<ConstraintViolation<WithdrawMoneyRequestDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
