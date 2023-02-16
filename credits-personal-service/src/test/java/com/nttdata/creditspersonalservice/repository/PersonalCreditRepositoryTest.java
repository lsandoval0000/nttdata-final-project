package com.nttdata.creditspersonalservice.repository;

import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import com.nttdata.creditspersonalservice.util.DefaultValues;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PersonalCreditRepositoryTest {

    @Autowired
    private PersonalCreditRepository underTest;

    @BeforeEach
    void setup() {
        underTest.save(PersonalCredit
                .builder()
                .dni("47081541")
                .creditAmount(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .amountLeft(DefaultValues.PERSONAL_CREDIT_LIMIT)
                .isActive(true)
                .initialDateToPay(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .monthsToPay(15)
                .monthlyFee(BigDecimal.valueOf(1500L))
                .interestRate(BigDecimal.valueOf(5.5D))
                .latePaymentInterest(BigDecimal.valueOf(2.5D))
                .build());
    }

    @AfterEach
    void cleanUp() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("Debe encontrar el crédito activo en base al DNI indicado")
    void shouldFindActiveCreditByDni() {
        String dni = "47081541";

        Optional<PersonalCredit> activeCredit = underTest.findActiveCreditByDni(dni);

        assertEquals(true, activeCredit.isPresent());
    }

    @Test
    @DisplayName("Debe contar el número de créditos activo en base al DNI indicado")
    void shouldCountActivePersonalCreditByDni() {
        String dni = "47081541";

        Long countActivePersonalCreditByDni = underTest.countActivePersonalCreditByDni(dni);

        assertEquals(1, countActivePersonalCreditByDni);
    }
}