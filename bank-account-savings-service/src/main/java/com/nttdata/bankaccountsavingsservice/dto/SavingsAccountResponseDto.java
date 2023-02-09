package com.nttdata.bankaccountsavingsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The savings account response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountResponseDto implements Serializable {
    private SavingsAccountDto savingsAccount;
    private String message;
}
