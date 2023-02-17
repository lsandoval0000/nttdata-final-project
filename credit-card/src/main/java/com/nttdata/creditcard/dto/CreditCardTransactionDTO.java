package com.nttdata.creditcard.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Credit card transaction dto.
 */
@Data
public class CreditCardTransactionDTO implements Serializable {
    private int creditCardId;
    private String description;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;
}
