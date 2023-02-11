package com.nttdata.bankaccountsavingsservice.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The transaction detail dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDataDto implements Serializable {

    private Long totalElements;
    private Integer totalPages;
    List<TransactionDto> transactions;
}
