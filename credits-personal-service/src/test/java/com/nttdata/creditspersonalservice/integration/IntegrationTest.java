package com.nttdata.creditspersonalservice.integration;

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
    @DisplayName("El cliente puede solicitar un nuevo crédito cuando los datos son correctos")
    void canRequestCredit() throws Exception {
        webTestClient.post()
                .uri("/api/credits/personal")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "    \"dni\":\"47081541\"," +
                        "    \"clientType\":\"PERSONAL\"," +
                        "    \"creditAmount\":30000.00," +
                        "    \"initialDateToPay\":\"2023-02-24\"," +
                        "    \"monthsToPay\":15," +
                        "    \"monthlyFee\":1500," +
                        "    \"interestRate\":5.55," +
                        "    \"latePaymentInterest\": 0.60" +
                        "}")
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    @Order(2)
    @DisplayName("Puede pagar el crédito cuando los datos son válidos")
    void canPayCredit() throws Exception {
        String dni = "47081541";
        webTestClient.post().uri("/api/credits/personal/{dni}/paid", dni)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "    \"paymentMethod\":\"SAVINGS_ACCOUNT\"," +
                        "    \"amountToPay\": 2000" +
                        "}")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(3)
    @DisplayName("Puede obtener el balance del crédito cuando existe dicho crédito")
    void canGetCreditBalance() throws Exception {
        String dni = "47081541";

        webTestClient.get().uri("/api/credits/personal/{dni}/balance", dni)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(4)
    @DisplayName("Puede obtener las transacciones del crédito cuando existe dicho crédito")
    void canGetCreditTransactionHistory() throws Exception {
        String dni = "47081541";

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/credits/personal/{dni}/transactions")
                        .queryParam("page", "1")
                        .queryParam("page_size", "10")
                        .build(dni))
                .exchange()
                .expectStatus()
                .isOk();
    }
}