package com.nttdata.bankaccountsavingsservice.service.implementation;

import com.nttdata.bankaccountsavingsservice.controller.exception.InvalidValueProvidedException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.bankaccountsavingsservice.dto.mapper.TransactionDtoMapper;
import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDataDto;
import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
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
     * Gets account transaction history.
     *
     * @param dni      the dni
     * @param page     the page
     * @param pageSize the page size
     * @return the account transaction history
     */
    @Override
    public TransactionDataDto getAccountTransactionHistory(String dni, Integer page, Integer pageSize) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);

        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }

        if (page < 0 || pageSize < 0) {
            throw new InvalidValueProvidedException(
                    "El valor del parámetro page y pageSize deben ser enteros positivos");
        }

        Long accountId = savingsAccountRepository.getAccountIdByDni(savingsAccount.getDni());
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
