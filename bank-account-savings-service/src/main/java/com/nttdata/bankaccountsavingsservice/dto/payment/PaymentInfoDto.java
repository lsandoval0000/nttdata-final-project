package com.nttdata.bankaccountsavingsservice.dto.payment;

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
 * The payment info dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDto implements Serializable {
    @NotBlank(message = "Service to pay is required.")
    private String serviceToPay;
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount to pay must be grater than 0.")
    @Digits(integer = 10, fraction = 2, message = "Amount to pay must have two decimals or less.")
    private BigDecimal amountToPay;
}
