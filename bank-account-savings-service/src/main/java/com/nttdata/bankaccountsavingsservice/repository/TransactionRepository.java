package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The transaction repository.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Find all transactions by savings account id.
     *
     * @param accountId the account id
     * @param pageable  the pageable
     * @return the page
     */
    @Query("select t from Transaction t where t.savingsAccount.accountId = ?1")
    Page<Transaction> findAllBySavingsAccount(Long accountId, Pageable pageable);

    /**
     * Count all transactions of current month.
     *
     * @return the long
     */
    @Query("select count(t) " +
            "from Transaction t " +
            "where year(t.transactionDate) = year(current_date) and  month(t.transactionDate) = month(current_date)" +
            " and t.description like '%retira%' and t.savingsAccount.accountId = ?1")
    Integer countAllWithdrawTransactionsOfCurrentMonthBySavingsAccount(Long accountId);
}
