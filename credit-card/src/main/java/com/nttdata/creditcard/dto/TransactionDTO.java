package com.nttdata.creditcard.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Transaction dto.
 */
@Data
public class TransactionDTO implements Serializable {
    private String date;
    private String description;
    private BigDecimal amount;
    private String action;
}
