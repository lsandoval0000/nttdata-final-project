package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The savings account repository.
 */
@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
}
