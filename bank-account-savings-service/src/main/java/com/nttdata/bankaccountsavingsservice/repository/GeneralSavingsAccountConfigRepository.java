package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.model.GeneralSavingsAccountConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The general savings account config repository.
 */
public interface GeneralSavingsAccountConfigRepository extends JpaRepository<GeneralSavingsAccountConfigEntity, Long> {
}
