package com.nttdata.bankaccountsavingsservice.dto.newaccount;

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
 * The savings account request dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewSavingsAccountRequestDto implements Serializable {
    @NotBlank(message = "Dni is required.")
    private String dni;
    @NotBlank(message = "Client type is required.")
    private String clientType;
    @DecimalMin(value = "0.0", message = "Initial amount must be 0 or greater.")
    @Digits(integer = 10, fraction = 2, message = "Initial amount must have two decimals or less.")
    private BigDecimal initialAmount;
}
