package com.nttdata.creditspersonalservice.service;

import com.nttdata.creditspersonalservice.dto.transaction.TransactionDataDto;

/**
 * The transaction service.
 */
public interface TransactionService {
    /**
     * Gets credit transaction history.
     *
     * @param dni      the dni
     * @param page     the page
     * @param pageSize the page size
     * @return the credit transaction history
     */
    TransactionDataDto getCreditTransactionHistory(String dni, Integer page, Integer pageSize);
}
