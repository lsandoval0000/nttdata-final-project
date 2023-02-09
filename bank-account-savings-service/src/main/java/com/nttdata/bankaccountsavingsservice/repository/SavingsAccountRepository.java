package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The savings account repository.
 */
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
}
