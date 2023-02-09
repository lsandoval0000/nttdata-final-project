package com.nttdata.bankaccountsavingsservice.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * The transaction detail dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailDto implements Serializable {
    private OffsetDateTime date;
    private String operation;
    private BigDecimal amount;
}
