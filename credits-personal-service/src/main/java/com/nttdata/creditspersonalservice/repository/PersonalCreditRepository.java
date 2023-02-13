package com.nttdata.creditspersonalservice.repository;

import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The personal credit repository.
 */
@Repository
public interface PersonalCreditRepository extends JpaRepository<PersonalCredit, Long> {
    /**
     * Find by dni.
     *
     * @param dni the dni
     * @return the optional
     */
    @Query("select p from PersonalCredit p where p.dni = ?1 and p.isActive = true")
    Optional<PersonalCredit> findActiveCreditByDni(String dni);

    /**
     * Gets personal credit id by dni.
     *
     * @param dni the dni
     * @return the personal credit id by dni
     */
    @Query("select p.creditId from PersonalCredit p where p.dni = ?1 and p.isActive = true")
    Long getPersonalCreditIdByDni(String dni);

    /**
     * Count active personal credit by dni.
     *
     * @param dni the dni
     * @return the long
     */
    @Query("select count(p) from PersonalCredit p where p.dni = ?1 and p.isActive = true")
    Long countActivePersonalCreditByDni(String dni);
}
