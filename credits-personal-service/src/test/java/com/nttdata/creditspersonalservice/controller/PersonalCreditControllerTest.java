package com.nttdata.creditspersonalservice.controller;

import com.nttdata.creditspersonalservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.creditspersonalservice.dto.PersonalCreditResponseDto;
import com.nttdata.creditspersonalservice.dto.transaction.TransactionDataDto;
import com.nttdata.creditspersonalservice.service.implementation.PersonalCreditServiceImpl;
import com.nttdata.creditspersonalservice.service.implementation.TransactionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonalCreditController.class)
class PersonalCreditControllerTest {

    @MockBean
    private PersonalCreditServiceImpl personalCreditService;
    @MockBean
    private TransactionServiceImpl transactionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("El cliente puede solicitar un nuevo crédito cuando los datos son correctos")
    void canRequestCreditWhenDataIsValid() throws Exception {
        mockMvc.perform(post("/api/credits/personal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"dni\":\"47081541\"," +
                                "    \"clientType\":\"PERSONAL\"," +
                                "    \"creditAmount\":30000.00," +
                                "    \"initialDateToPay\":\"2023-02-24\"," +
                                "    \"monthsToPay\":15," +
                                "    \"monthlyFee\":1500," +
                                "    \"interestRate\":5.55," +
                                "    \"latePaymentInterest\": 0.60" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Falla cuando se trata de solicitar un crédito con datos inválidos")
    void failWhenTryToRequestCreditWithInvalidData() throws Exception {
        mockMvc.perform(post("/api/credits/personal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"dni\":\"47081541\"," +
                                "    \"clientType\":\"PERSONAL\"," +
                                "    \"creditAmount\":-30000.00," +
                                "    \"initialDateToPay\":\"2023-02-24\"," +
                                "    \"monthsToPay\":-15," +
                                "    \"monthlyFee\":1500," +
                                "    \"interestRate\":5.55," +
                                "    \"latePaymentInterest\": 0.60" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Puede pagar el crédito cuando los datos son válidos")
    void canPayCreditWhenDataIsValid() throws Exception {
        String dni = "47081541";
        mockMvc.perform(post("/api/credits/personal/{dni}/paid", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"paymentMethod\":\"SAVINGS_ACCOUNT\"," +
                                "    \"amountToPay\": 2000" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Falla cuando el crédito a pagar no existe")
    void failWhenThereIsNoActiveCredit() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee un crédito activo."))
                .when(personalCreditService)
                .payCredit(any(), any());

        mockMvc.perform(post("/api/credits/personal/{dni}/paid", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"paymentMethod\":\"SAVINGS_ACCOUNT\"," +
                                "    \"amountToPay\": 2000" +
                                "}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Puede obtener el balance del crédito cuando existe dicho crédito")
    void canGetCreditBalance() throws Exception {
        String dni = "47081541";

        when(personalCreditService.getCreditBalance(dni))
                .thenReturn(PersonalCreditResponseDto
                        .builder()
                        .message("Consulta realizada exitosamente")
                        .build());

        mockMvc.perform(get("/api/credits/personal/{dni}/balance", dni)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("Falla cuando se trata de obtener el balance de un crédito que no existe")
    void failWhenThereIsNoActiveCreditToGetBalance() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee un crédito activo."))
                .when(personalCreditService)
                .getCreditBalance(dni);

        mockMvc.perform(get("/api/credits/personal/{dni}/balance", dni)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Puede obtener las transacciones del crédito cuando existe dicho crédito")
    void canGetCreditTransactionHistory() throws Exception {
        String dni = "47081541";

        when(transactionService.getCreditTransactionHistory(any(), any(), any()))
                .thenReturn(TransactionDataDto
                        .builder()
                        .totalElements(10L)
                        .totalPages(1)
                        .build());

        mockMvc.perform(get("/api/credits/personal/{dni}/transactions", dni)
                        .queryParam("page", "1")
                        .queryParam("page_size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber());
    }

    @Test
    @DisplayName("Falla cuando se trata de obtener las transacciones de un crédito que no existe")
    void failWhenThereIsNoActiveCreditToGetTransactions() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee un crédito activo."))
                .when(transactionService)
                .getCreditTransactionHistory(any(), any(), any());

        mockMvc.perform(get("/api/credits/personal/{dni}/transactions", dni)
                        .queryParam("page", "1")
                        .queryParam("page_size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}