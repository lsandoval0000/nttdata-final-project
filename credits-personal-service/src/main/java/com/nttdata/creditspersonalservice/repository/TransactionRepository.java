package com.nttdata.creditspersonalservice.repository;

import com.nttdata.creditspersonalservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The transaction repository.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Find all by personal credit id.
     *
     * @param creditId the credit id
     * @param pageable the pageable
     * @return the page
     */
    @Query("select t from Transaction t where t.personalCredit.creditId = ?1")
    Page<Transaction> findAllByPersonalCreditId(Long creditId, Pageable pageable);
}
