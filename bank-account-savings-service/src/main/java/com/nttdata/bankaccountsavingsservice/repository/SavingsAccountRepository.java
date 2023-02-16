package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The savings account repository.
 */
@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    /**
     * Count savings account by dni.
     *
     * @param dni the dni
     * @return the count
     */
    @Query("select count(s) from SavingsAccount s where s.dni = ?1")
    Long countSavingsAccountByDni(String dni);

    /**
     * Find a savings account by dni.
     *
     * @param dni the dni
     * @return the savings account
     */
    Optional<SavingsAccount> findSavingsAccountByDni(String dni);

    /**
     * Gets account id by dni.
     *
     * @param dni the dni
     * @return the account id
     */
    @Query("select s.accountId from SavingsAccount s where s.dni = ?1")
    Long getAccountIdByDni(String dni);
}
