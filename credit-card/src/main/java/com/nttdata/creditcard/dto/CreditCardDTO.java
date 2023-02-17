package com.nttdata.creditcard.dto;

import com.nttdata.creditcard.util.CreditCardStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Credit card dto.
 */
@Data
public class CreditCardDTO implements Serializable {
    private int id;
    private String accountNumber;
    private String cardNumber;
    private BigDecimal creditLimit;
    private BigDecimal amountConsumed;
    @Enumerated(EnumType.STRING)
    private CreditCardStatus status;
    private String dni;
}
