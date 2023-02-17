package com.nttdata.creditcard.service;

import com.nttdata.creditcard.dto.TransactionDTO;
import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.entity.Transaction;
import com.nttdata.creditcard.repository.CreditCardRepository;
import com.nttdata.creditcard.repository.TransactionRepository;
import com.nttdata.creditcard.util.ConvertEntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Transaction service.
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;
    /**
     * Get transaction.
     *
     * @param transaction the transaction
     * @return the transaction
     */
    @Override
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    @Override
    public List<TransactionDTO> transactionsByAccount(String accountNumber) {
        CreditCard creditCard = creditCardRepository.findByAccountNumber(accountNumber);
        return transactionRepository.findAllByAccount(creditCard.getId())
                .stream()
                .map(transaction -> convertEntityToDTO.convertToTransactionDTO(transaction))
                .collect(Collectors.toList());
    }
}
