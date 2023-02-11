package com.nttdata.bankaccountsavingsservice.service;

import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDataDto;

/**
 * The transaction service.
 */
public interface TransactionService {
    /**
     * Gets account transaction history.
     *
     * @param dni      the dni
     * @param page     the page
     * @param pageSize the page size
     * @return the account transaction history
     */
    TransactionDataDto getAccountTransactionHistory(String dni, Integer page, Integer pageSize);
}
