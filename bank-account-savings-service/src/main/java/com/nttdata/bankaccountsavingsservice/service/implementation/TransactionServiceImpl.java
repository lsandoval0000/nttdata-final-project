package com.nttdata.bankaccountsavingsservice.service.implementation;

import com.nttdata.bankaccountsavingsservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.bankaccountsavingsservice.dto.mapper.TransactionDtoMapper;
import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDataDto;
import com.nttdata.bankaccountsavingsservice.entity.Transaction;
import com.nttdata.bankaccountsavingsservice.repository.SavingsAccountRepository;
import com.nttdata.bankaccountsavingsservice.repository.TransactionRepository;
import com.nttdata.bankaccountsavingsservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * The transaction service.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionDtoMapper transactionDtoMapper;

    /**
     * @param dni
     * @param page
     * @param pageSize
     * @return TransactionDataDto
     */
    @Override
    public TransactionDataDto getAccountTransactionHistory(String dni, Integer page, Integer pageSize) {
        Integer clientExists = savingsAccountRepository.countSavingsAccountByDni(dni);
        if (clientExists.equals(0)) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }
        Long accountId = savingsAccountRepository.getAccountIdByDni(dni);
        Page<Transaction> transactions = transactionRepository
                .findAllBySavingsAccount(accountId, PageRequest.of(page, pageSize));

        return TransactionDataDto
                .builder()
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .transactions(transactionDtoMapper.convertToDtoList(transactions.getContent()))
                .build();
    }
}
