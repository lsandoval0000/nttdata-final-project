package com.nttdata.creditspersonalservice.service;

import com.nttdata.creditspersonalservice.controller.exception.InvalidValueProvidedException;
import com.nttdata.creditspersonalservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.creditspersonalservice.dto.mapper.TransactionDtoMapper;
import com.nttdata.creditspersonalservice.dto.transaction.TransactionDataDto;
import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import com.nttdata.creditspersonalservice.entity.Transaction;
import com.nttdata.creditspersonalservice.repository.PersonalCreditRepository;
import com.nttdata.creditspersonalservice.repository.TransactionRepository;
import com.nttdata.creditspersonalservice.service.implementation.TransactionServiceImpl;
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
    private TransactionRepository transactionRepository;
    @Mock
    private PersonalCreditRepository personalCreditRepository;
    private TransactionDtoMapper transactionDtoMapper;
    private TransactionService underTest;

    @BeforeEach
    void setup() {
        transactionDtoMapper = new TransactionDtoMapper();
        underTest = new TransactionServiceImpl(
                transactionRepository,
                personalCreditRepository,
                transactionDtoMapper);
    }

    @Test
    @DisplayName("Debe retornar el historial de las transacciones del cr??dito seg??n el dni" +
            " y los valores de paginaci??n")
    void shouldReturnCreditTransactionHistoryByDniAndPaginationValues() {
        String dni = "47081541";
        PageRequest pageRequest = PageRequest.of(0, 5);
        PersonalCredit personalCredit = PersonalCredit.builder().build();
        List<Transaction> transactionEntityList = List.of(
                Transaction.builder().build(),
                Transaction.builder().build(),
                Transaction.builder().build(),
                Transaction.builder().build(),
                Transaction.builder().build()
        );
        Page<Transaction> transactionEntityPage = new PageImpl<>(transactionEntityList);

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.of(personalCredit));
        given(transactionRepository.findAllByPersonalCreditId(personalCredit.getCreditId(), pageRequest))
                .willReturn(transactionEntityPage);

        TransactionDataDto returnedValue = underTest
                .getCreditTransactionHistory(dni, pageRequest.getPageNumber(), pageRequest.getPageSize());

        assertEquals(transactionEntityPage.getContent().size(), returnedValue.getTransactions().size());
        assertEquals(transactionEntityPage.getTotalPages(), returnedValue.getTotalPages());
        assertEquals(transactionEntityList.size(), returnedValue.getTotalElements());
    }

    @Test
    @DisplayName("Debe generar la excepci??n NoSuchElementFoundException cuando el cliente no posee un cr??dito activo")
    void shouldRaiseNoSuchElementFoundExceptionWhenClientDoesNotHaveActiveCredit() {
        String dni = "47081541";

        given(personalCreditRepository.findActiveCreditByDni(dni)).willReturn(Optional.empty());

        assertThrows(
                NoSuchElementFoundException.class, () -> underTest.getCreditTransactionHistory(dni, 0, 5));
        verify(transactionRepository, never()).findAllByPersonalCreditId(any(), any());
    }

    @Test
    @DisplayName("Debe generar la excepci??n InvalidValueProvidedException cuando la p??gina o el tama??o de p??gina " +
            "no son v??lidos")
    void shouldRaiseInvalidValueProvidedExceptionWhenPageOrPageSizeAreNotValid() {
        String dni = "47081541";

        given(personalCreditRepository.findActiveCreditByDni(dni))
                .willReturn(Optional.of(PersonalCredit.builder().build()));

        assertThrows(
                InvalidValueProvidedException.class,
                () -> underTest.getCreditTransactionHistory(dni, 0, -5));
        verify(transactionRepository, never()).findAllByPersonalCreditId(any(), any());
    }
}