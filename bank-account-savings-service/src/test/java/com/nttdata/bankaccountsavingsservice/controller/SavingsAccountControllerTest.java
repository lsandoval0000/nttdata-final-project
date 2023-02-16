package com.nttdata.bankaccountsavingsservice.controller;

import com.nttdata.bankaccountsavingsservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDataDto;
import com.nttdata.bankaccountsavingsservice.service.SavingsAccountService;
import com.nttdata.bankaccountsavingsservice.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SavingsAccountController.class)
class SavingsAccountControllerTest {

    @MockBean
    private SavingsAccountService savingsAccountService;
    @MockBean
    private TransactionService transactionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("El cliente puede solicitar la apertura de una cuenta de ahorros")
    void canRequestNewSavingsAccountWhenDataIsValid() throws Exception {
        mockMvc.perform(post("/api/bank-account/savings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"dni\":\"47081541\"," +
                                "    \"clientType\":\"PERSONAL\"," +
                                "    \"initialAmount\":25000.34" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Falla al tratar de solicitar la apertura de una cuenta de ahorros con datos inválidos")
    void failToRequestNewSavingsAccountWhenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/api/bank-account/savings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"dni\":\"\"," +
                                "    \"clientType\":\"PERSONAL\"," +
                                "    \"initialAmount\":-25000.34" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("El cliente puede depositar dinero en la cuenta de ahorros")
    void canDepositMoneyIntoAccount() throws Exception {
        String dni = "47081541";
        mockMvc.perform(post("/api/bank-account/savings/{dni}/deposit", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"amount\": 1500.23" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Falla al tratar de realizar un depósito en una cuenta de ahorro inexistente")
    void failWhenTryToDepositMoneyIntoNonExistingSavingsAccount() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros."))
                .when(savingsAccountService)
                .depositMoneyIntoAccount(any(), any());

        mockMvc.perform(post("/api/bank-account/savings/{dni}/deposit", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"amount\": 1500.23" +
                                "}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("El cliente puede realizar retiros de su cuenta de ahorros")
    void canWithdrawMoneyFromAccount() throws Exception {
        String dni = "47081541";
        mockMvc.perform(post("/api/bank-account/savings/{dni}/withdrawal", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"amount\": 5.00" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Falla al tratar de realizar un retiro de una cuenta de ahorros inexistente")
    void failWhenTryToWithdrawMoneyFromNonExistingSavingsAccount() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros."))
                .when(savingsAccountService)
                .withdrawMoneyFromAccount(any(), any());

        mockMvc.perform(post("/api/bank-account/savings/{dni}/withdrawal", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"amount\": 5.00" +
                                "}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("El cliente puede pagar servicios empleando su cuenta de ahorros")
    void canPayUsingAccount() throws Exception {
        String dni = "47081541";
        mockMvc.perform(post("/api/bank-account/savings/{dni}/payment", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"serviceToPay\":\"Pago de cuota de crédito solicitado\"," +
                                "    \"amountToPay\":523.4" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Falla al intentar realizar un pago de un servicio con una cuenta de ahorros inexistente")
    void failWhenTryToPayAServiceWithNonExistingSavingsAccount() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros."))
                .when(savingsAccountService)
                .payUsingAccount(any(), any());

        mockMvc.perform(post("/api/bank-account/savings/{dni}/payment", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "    \"serviceToPay\":\"Pago de cuota de crédito solicitado\"," +
                                "    \"amountToPay\":523.4" +
                                "}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("El cliente puede obtener el balance de su cuenta de ahorros")
    void canGetAccountBalance() throws Exception {
        String dni = "47081541";

        when(savingsAccountService.getAccountBalance(dni))
                .thenReturn(SavingsAccountResponseDto
                        .builder()
                        .message("Consulta realizada exitosamente")
                        .build());

        mockMvc.perform(get("/api/bank-account/savings/{dni}/balance", dni)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("Falla al intentar obtener el balance de una cuenta de ahorros inexistente")
    void failToGetBalanceFromNonExistingSavingsAccount() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros."))
                .when(savingsAccountService)
                .getAccountBalance(any());

        mockMvc.perform(get("/api/bank-account/savings/{dni}/balance", dni)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("El cliente puede obtener un historial de transacciones de su cuenta de ahorros")
    void canGetAccountTransactionHistory() throws Exception {
        String dni = "47081541";

        when(transactionService.getAccountTransactionHistory(any(), any(), any()))
                .thenReturn(TransactionDataDto
                        .builder()
                        .totalElements(10L)
                        .totalPages(1)
                        .build());

        mockMvc.perform(get("/api/bank-account/savings/{dni}/transactions", dni)
                        .queryParam("page", "1")
                        .queryParam("page_size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber());
    }

    @Test
    @DisplayName("Falla al intentar obtener el historial de transacciones de una cuenta de ahorros inexistente")
    void failToGetTransactionsFromNonExistingSavingsAccount() throws Exception {
        String dni = "47081541";

        doThrow(new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros."))
                .when(transactionService)
                .getAccountTransactionHistory(any(), any(), any());

        mockMvc.perform(get("/api/bank-account/savings/{dni}/transactions", dni)
                        .queryParam("page", "1")
                        .queryParam("page_size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}