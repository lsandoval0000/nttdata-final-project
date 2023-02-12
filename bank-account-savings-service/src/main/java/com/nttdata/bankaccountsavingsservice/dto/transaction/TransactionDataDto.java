package com.nttdata.bankaccountsavingsservice.dto.transaction;

import lombok.*;

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
    @ToString.Exclude
    private List<TransactionDto> transactions;
}
