package com.nttdata.bankaccountsavingsservice.service;

import com.nttdata.bankaccountsavingsservice.controller.exception.InvalidValueProvidedException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.bankaccountsavingsservice.dto.mapper.TransactionDtoMapper;
import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDataDto;
import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import com.nttdata.bankaccountsavingsservice.entity.Transaction;
import com.nttdata.bankaccountsavingsservice.repository.SavingsAccountRepository;
import com.nttdata.bankaccountsavingsservice.repository.TransactionRepository;
import com.nttdata.bankaccountsavingsservice.service.implementation.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private SavingsAccountRepository savingsAccountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    private TransactionDtoMapper transactionDtoMapper;
    private TransactionService underTest;

    @BeforeEach
    void setup() {
        transactionDtoMapper = new TransactionDtoMapper();
        underTest = new TransactionServiceImpl(
                savingsAccountRepository,
                transactionRepository,
                transactionDtoMapper);
    }

    @Test
    @DisplayName("Debe retornar el historial de las transacciones de la cuenta de ahorro según el dni" +
            " y los valores de paginación")
    void shouldReturnAccountTransactionHistoryByDniAndPaginationValues() {
        String dni = "47081541";
        PageRequest pageRequest = PageRequest.of(0, 5);
        SavingsAccount savingsAccount = SavingsAccount.builder().build();
        List<Transaction> transactionEntityList = List.of(
                Transaction.builder().build(),
                Transaction.builder().build(),
                Transaction.builder().build(),
                Transaction.builder().build(),
                Transaction.builder().build()
        );
        Page<Transaction> transactionEntityPage = new PageImpl<>(transactionEntityList);

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.of(savingsAccount));
        given(transactionRepository.findAllBySavingsAccount(any(), any()))
                .willReturn(transactionEntityPage);

        TransactionDataDto returnedValue = underTest
                .getAccountTransactionHistory(dni, pageRequest.getPageNumber(), pageRequest.getPageSize());

        assertEquals(transactionEntityPage.getContent().size(), returnedValue.getTransactions().size());
        assertEquals(transactionEntityPage.getTotalPages(), returnedValue.getTotalPages());
        assertEquals(transactionEntityList.size(), returnedValue.getTotalElements());
    }

    @Test
    @DisplayName("Debe generar la excepción NoSuchElementFoundException cuando el cliente no posee uan cuenta de ahorro")
    void shouldRaiseNoSuchElementFoundExceptionWhenClientDoesNotHaveSavingsAccount() {
        String dni = "47081541";

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.empty());

        assertThrows(
                NoSuchElementFoundException.class, () -> underTest.getAccountTransactionHistory(dni, 0, 5));
        verify(transactionRepository, never()).findAllBySavingsAccount(any(), any());
    }

    @Test
    @DisplayName("Debe generar la excepción InvalidValueProvidedException cuando la página o el tamaño de página " +
            "no son válidos")
    void shouldRaiseInvalidValueProvidedExceptionWhenPageOrPageSizeAreNotValid() {
        String dni = "47081541";

        given(savingsAccountRepository.findSavingsAccountByDni(dni))
                .willReturn(Optional.of(SavingsAccount.builder().build()));

        assertThrows(
                InvalidValueProvidedException.class,
                () -> underTest.getAccountTransactionHistory(dni, 0, -5));
        verify(transactionRepository, never()).findAllBySavingsAccount(any(), any());
    }
}