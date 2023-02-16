package com.nttdata.creditspersonalservice.service.implementation;

import com.nttdata.creditspersonalservice.controller.exception.InvalidValueProvidedException;
import com.nttdata.creditspersonalservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.creditspersonalservice.dto.mapper.TransactionDtoMapper;
import com.nttdata.creditspersonalservice.dto.transaction.TransactionDataDto;
import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import com.nttdata.creditspersonalservice.entity.Transaction;
import com.nttdata.creditspersonalservice.repository.PersonalCreditRepository;
import com.nttdata.creditspersonalservice.repository.TransactionRepository;
import com.nttdata.creditspersonalservice.service.TransactionService;
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

    private final TransactionRepository transactionRepository;
    private final PersonalCreditRepository personalCreditRepository;
    private final TransactionDtoMapper transactionDtoMapper;

    /**
     * Gets credit transaction history.
     *
     * @param dni      the dni
     * @param page     the page
     * @param pageSize the page size
     * @return the credit transaction history
     */
    @Override
    public TransactionDataDto getCreditTransactionHistory(String dni, Integer page, Integer pageSize) {
        PersonalCredit personalCredit = personalCreditRepository.findActiveCreditByDni(dni).orElse(null);

        if (personalCredit == null) {
            throw new NoSuchElementFoundException("El cliente no posee un crédito activo.");
        }

        if (page < 0 || pageSize < 0) {
            throw new InvalidValueProvidedException(
                    "El valor del parámetro page y pageSize deben ser enteros positivos");
        }

        Page<Transaction> transactions = transactionRepository
                .findAllByPersonalCreditId(personalCredit.getCreditId(), PageRequest.of(page, pageSize));

        return TransactionDataDto
                .builder()
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .transactions(transactionDtoMapper.convertToDtoList(transactions.getContent()))
                .build();
    }
}
