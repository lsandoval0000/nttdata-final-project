package com.nttdata.creditspersonalservice.controller.validation;

import com.nttdata.creditspersonalservice.dto.payment.PaymentInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentInfoTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("El servicio a pagar no debe ser nulo")
    void paymentMethodMustNotBeNull() {
        PaymentInfoDto request = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(0L))
                .build();

        Set<ConstraintViolation<PaymentInfoDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("El monto a pagar debe ser mayor a cero")
    void amountToPayMustBeGreaterThanZero() {
        PaymentInfoDto request = PaymentInfoDto
                .builder()
                .paymentMethod("TEST")
                .amountToPay(BigDecimal.valueOf(-2.32D))
                .build();

        Set<ConstraintViolation<PaymentInfoDto>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
