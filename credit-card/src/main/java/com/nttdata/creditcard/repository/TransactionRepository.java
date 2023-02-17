package com.nttdata.creditcard.repository;

import com.nttdata.creditcard.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Transaction repository.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    /**
     * Find all by account list.
     *
     * @param creditCardId the credit card id
     * @return the list
     */
    @Query("select t from Transaction t where t.creditCard.id = ?1")
    List<Transaction> findAllByAccount(Integer creditCardId);
}
