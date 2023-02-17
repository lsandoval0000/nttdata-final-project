package com.nttdata.creditcard.service;

import com.nttdata.creditcard.dto.TransactionDTO;
import com.nttdata.creditcard.entity.Transaction;

import java.util.List;

/**
 * The interface Transaction service.
 */
public interface TransactionService {
    /**
     * Create transaction.
     *
     * @param transaction the transaction
     * @return the transaction
     */
    public Transaction create(Transaction transaction);

    /**
     * Transactions by account list.
     *
     * @param accountNumber the account number
     * @return the list
     */
    public List<TransactionDTO> transactionsByAccount(String accountNumber);
}
