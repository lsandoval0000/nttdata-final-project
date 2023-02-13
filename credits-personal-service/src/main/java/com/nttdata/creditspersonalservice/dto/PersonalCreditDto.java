package com.nttdata.creditspersonalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The personal credit dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCreditDto implements Serializable {
    private BigDecimal creditAmount;
    private BigDecimal amountLeft;
    private Date initialDateToPay;
    private Integer monthsToPay;
    private BigDecimal monthlyFee;
    private BigDecimal interestRate;
    private BigDecimal latePaymentInterest;
}
