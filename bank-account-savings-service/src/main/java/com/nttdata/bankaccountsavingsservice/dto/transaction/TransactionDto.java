package com.nttdata.bankaccountsavingsservice.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * The Movement dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto implements Serializable {
    private OffsetDateTime transactionDate;
    private String description;
    private BigDecimal amount;
}
