package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import com.nttdata.bankaccountsavingsservice.util.AccountNumberGenerator;
import com.nttdata.bankaccountsavingsservice.util.DefaultValues;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SavingsAccountRepositoryTest {

    @Autowired
    private SavingsAccountRepository underTest;

    @BeforeEach
    void setup() {
        List<SavingsAccount> savingsAccountList = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            BigDecimal temporal = BigDecimal.valueOf((index + 1) * 10);
            savingsAccountList.add(SavingsAccount
                    .builder()
                    .dni("4708154" + index)
                    .accountNumber(AccountNumberGenerator.generateAccountNumber())
                    .balance(temporal)
                    .monthlyAvailableMovements(DefaultValues.MONTHLY_AVAILABLE_MOVEMENTS)
                    .maintenanceFee(DefaultValues.MAINTENANCE_FEE)
                    .build());
        }
        underTest.saveAll(savingsAccountList);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("Debe permitir contar el número de cuentas de ahorro registradas con el mismo número de dni")
    void shouldCountSavingsAccountByDni() {
        String dni = "47081541";

        Long countSavingsAccountByDni = underTest.countSavingsAccountByDni(dni);

        assertEquals(1L, countSavingsAccountByDni);
    }

    @Test
    @DisplayName("Debe permitir obtener los datos de una cuenta de ahorro empleando el número de dni")
    void shouldFindSavingsAccountByDni() {
        String dni = "47081541";

        Optional<SavingsAccount> savingsAccount = underTest.findSavingsAccountByDni(dni);

        assertTrue(savingsAccount.isPresent());
    }

    @Test
    @DisplayName("Debe permitir obtener el id de la cuenta de ahorro empleando el número de dni")
    void shouldGetAccountIdByDni() {
        String dni = "47081541";
        Long accountId = underTest.getAccountIdByDni(dni);

        assertNotEquals(0L, accountId);
    }
}