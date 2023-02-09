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
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0.")
    @Digits(integer = 10, fraction = 2, message = "Initial amount must have two decimals or less.")
    private BigDecimal amount;
}
