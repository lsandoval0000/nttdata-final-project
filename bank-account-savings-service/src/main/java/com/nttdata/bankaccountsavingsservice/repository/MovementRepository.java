package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The movement repository.
 */
@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
}
