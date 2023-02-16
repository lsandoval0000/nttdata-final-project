package com.nttdata.bankaccountsavingsservice.repository;

import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import com.nttdata.bankaccountsavingsservice.entity.Transaction;
import com.nttdata.bankaccountsavingsservice.util.AccountNumberGenerator;
import com.nttdata.bankaccountsavingsservice.util.DefaultValues;
import com.nttdata.bankaccountsavingsservice.util.StringTemplates;
import org.apache.commons.lang.text.StrSubstitutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository underTest;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @BeforeEach
    void setup() {
        SavingsAccount savingsAccount = savingsAccountRepository.save(SavingsAccount
                .builder()
                .dni("47081541")
                .accountNumber(AccountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.valueOf(50000L))
                .monthlyAvailableMovements(DefaultValues.MONTHLY_AVAILABLE_MOVEMENTS)
                .maintenanceFee(DefaultValues.MAINTENANCE_FEE)
                .build());
        List<Transaction> transactions = new ArrayList<>();
        for (int index = 0; index < 15; index++) {
            BigDecimal temporal = BigDecimal.valueOf((index + 1) * 10);
            savingsAccount.setBalance(savingsAccount.getBalance().add(temporal));
            transactions.add(
                    Transaction
                            .builder()
                            .description(
                                    StrSubstitutor.replace(
                                            StringTemplates.TRANSACTION_DESCRIPTION_TEMPLATE,
                                            Map.ofEntries(
                                                    entry("dni", savingsAccount.getDni()),
                                                    entry("accion", "deposita"),
                                                    entry("monto", temporal.toString()),
                                                    entry("balance", savingsAccount.getBalance().toString())
                                            )))
                            .amount(temporal)
                            .savingsAccount(savingsAccount)
                            .build());
        }
        underTest.saveAll(transactions);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        savingsAccountRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe permitir obtener el número de transacciones de retiro en el mes actual")
    void shouldCountAllWithdrawTransactionsOfCurrentMonthBySavingsAccount() {
        SavingsAccount savingsAccount = savingsAccountRepository.findSavingsAccountByDni("47081541").get();

        Integer transactionCount = underTest
                .countAllWithdrawTransactionsOfCurrentMonthBySavingsAccount(savingsAccount.getAccountId());

        assertEquals(0, transactionCount);
    }

    @Test
    @DisplayName("Debe retornar un listado de las transacciones pertenecientes a una cuenta de ahorro según " +
            " el id de la cuenta de ahorro y los valores de paginación")
    void shouldFindAllTransactionsBySavingsAccount() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        SavingsAccount savingsAccount = savingsAccountRepository.findSavingsAccountByDni("47081541").get();

        Page<Transaction> transactions = underTest.findAllBySavingsAccount(savingsAccount.getAccountId(), pageRequest);

        assertEquals(5, transactions.getSize());
    }
}