package com.nttdata.bankaccountsavingsservice.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    @DisplayName("El cliente puede solicitar la apertura de una cuenta de ahorros")
    void canRequestNewSavingsAccountWhenDataIsValid() {
        webTestClient.post()
                .uri("/api/bank-account/savings")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "    \"dni\":\"47081541\"," +
                        "    \"clientType\":\"PERSONAL\"," +
                        "    \"initialAmount\":25000.34" +
                        "}")
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    @Order(2)
    @DisplayName("El cliente puede depositar dinero en la cuenta de ahorros")
    void canDepositMoneyIntoAccount() {
        String dni = "47081541";
        webTestClient.post()
                .uri("/api/bank-account/savings/{dni}/deposit", dni)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "    \"amount\": 1500.23" +
                        "}")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(3)
    @DisplayName("El cliente puede realizar retiros de su cuenta de ahorros")
    void canWithdrawMoneyFromAccount() {
        String dni = "47081541";
        webTestClient.post()
                .uri("/api/bank-account/savings/{dni}/withdrawal", dni)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "    \"amount\": 5.00" +
                        "}")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(4)
    @DisplayName("El cliente puede pagar servicios empleando su cuenta de ahorros")
    void canPayUsingAccount() {
        String dni = "47081541";
        webTestClient.post()
                .uri("/api/bank-account/savings/{dni}/payment", dni)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "    \"serviceToPay\":\"Pago de cuota de crédito solicitado\"," +
                        "    \"amountToPay\":523.4" +
                        "}")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(5)
    @DisplayName("El cliente puede obtener el balance de su cuenta de ahorros")
    void canGetAccountBalance() {
        String dni = "47081541";

        webTestClient.get()
                .uri("/api/bank-account/savings/{dni}/balance", dni)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(6)
    @DisplayName("El cliente puede obtener un historial de transacciones de su cuenta de ahorros")
    void canGetAccountTransactionHistory() {
        String dni = "47081541";

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/bank-account/savings/{dni}/transactions")
                        .queryParam("page", "1")
                        .queryParam("page_size", "10")
                        .build(dni))
                .exchange()
                .expectStatus()
                .isOk();
    }
}