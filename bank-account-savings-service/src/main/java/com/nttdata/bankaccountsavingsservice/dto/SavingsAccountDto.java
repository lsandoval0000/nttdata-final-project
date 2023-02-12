package com.nttdata.bankaccountsavingsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The savings account dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountDto implements Serializable {
    private String accountNumber;
    private BigDecimal balance;
    private Integer monthlyAvailableMovements;
    private BigDecimal maintenanceFee;
}
