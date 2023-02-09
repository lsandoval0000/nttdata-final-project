package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long> {
}
