package com.nttdata.bankaccountsavingsservice.dto.withdraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The withdrawal money request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawMoneyRequestDto implements Serializable {
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0.")
    @Digits(integer = 10, fraction = 2, message = "Amount must have two decimals or less.")
    private BigDecimal amount;
}
