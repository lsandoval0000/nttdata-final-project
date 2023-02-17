package com.nttdata.creditcard.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The New credit card dto.
 */
@Data
public class NewCreditCardDTO implements Serializable {
    private String cardNumber;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal creditLimit;
    private String dni;
}
