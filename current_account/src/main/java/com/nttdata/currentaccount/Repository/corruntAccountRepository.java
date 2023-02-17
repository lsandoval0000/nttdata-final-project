package com.nttdata.currentaccount.Repository;

import com.nttdata.currentaccount.Entity.corruntAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface corruntAccountRepository extends JpaRepository<corruntAccountRepository,Integer> {

    Optional<corruntAccountEntity> findByDni(String DNI);

}
