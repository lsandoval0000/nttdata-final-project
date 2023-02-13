package com.nttdata.creditspersonalservice.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Payment info dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDto implements Serializable {
    @NotBlank(message = "El nombre del método de pago es requerido.")
    private String paymentMethod;
    @DecimalMin(value = "1.0", inclusive = false, message = "El monto debe ser mayor a cero.")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener como máximo dos decimales.")
    private BigDecimal amountToPay;
}
