package com.nttdata.bankaccountsavingsservice.dto.deposit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The deposit money request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositMoneyRequestDto implements Serializable {
    @DecimalMin(value = "1.0", inclusive = false, message = "El monto debe ser mayor a cero.")
    @Digits(integer = 10, fraction = 2, message = "El monto inicial debe tener como máximo dos decimales.")
    private BigDecimal amount;
}
