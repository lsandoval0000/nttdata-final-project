package com.nttdata.creditspersonalservice.repository;

import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import com.nttdata.creditspersonalservice.entity.Transaction;
import com.nttdata.creditspersonalservice.util.DefaultValues;
import com.nttdata.creditspersonalservice.util.StringTemplates;
import org.apache.commons.lang.text.StrSubstitutor;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository underTest;
    @Autowired
    private PersonalCreditRepository personalCreditRepository;

    @BeforeEach
    void setup() {
        PersonalCredit personalCredit = personalCreditRepository.save(PersonalCredit
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
        List<Transaction> transactions = new ArrayList<>();
        for (int index = 0; index < 15; index++) {
            BigDecimal temporal = BigDecimal.valueOf((index + 1) * 10);
            transactions.add(
                    Transaction
                            .builder()
                            .description(
                                    StrSubstitutor.replace(
                                            StringTemplates.TRANSACTION_DESCRIPTION_TEMPLATE,
                                            Map.ofEntries(
                                                    entry("dni", personalCredit.getDni()),
                                                    entry("accion",
                                                            "realiza el pago del crédito, en efectivo, por un monto de"),
                                                    entry("monto", temporal.toString()),
                                                    entry("pendiente",
                                                            personalCredit.getAmountLeft().subtract(temporal).toString())
                                            )))
                            .amount(temporal)
                            .personalCredit(personalCredit)
                            .build());
        }
        underTest.saveAll(transactions);
    }

    @Test
    @DisplayName("Debe retornar un listado de las transacciones pertenecientes a un crédito según el id de crédito" +
            " y los valores de paginación")
    void shouldReturnTransactionListBasedOnCreditIdAndPaginationValues() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        PersonalCredit activeCredit = personalCreditRepository.findActiveCreditByDni("47081541").get();

        Page<Transaction> transactions = underTest.findAllByPersonalCreditId(activeCredit.getCreditId(), pageRequest);

        assertEquals(5, transactions.getSize());
    }
}