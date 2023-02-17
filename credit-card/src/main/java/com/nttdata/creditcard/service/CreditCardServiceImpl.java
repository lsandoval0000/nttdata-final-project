package com.nttdata.creditcard.service;

import com.nttdata.creditcard.dto.CreditCardDTO;
import com.nttdata.creditcard.dto.CreditCardTransactionDTO;
import com.nttdata.creditcard.dto.NewCreditCardDTO;
import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.entity.Transaction;
import com.nttdata.creditcard.repository.CreditCardRepository;
import com.nttdata.creditcard.util.*;
import com.nttdata.creditcard.util.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Credit card service.
 */
@Service
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private  ConvertDTOToEntity convertDTOToEntity;
    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;
    /**
     * Get credit card.
     *
     * @param dni the dni
     * @return the credit card dto
     */
    @Override
    public List<CreditCardDTO> creditCardsByDni(String dni) {
        return creditCardRepository.findByDni(dni)
                .stream()
                .map(creditCard -> convertEntityToDTO.convertToCreditCardDTO(creditCard))
                .collect(Collectors.toList());
    }
    /**
     * Get credit card.
     *
     * @param creditCardDTO the credit card
     * @return the credit card dto
     */
    @Override
    public CreditCardDTO create(NewCreditCardDTO creditCardDTO) {
        CreditCard creditCard = convertDTOToEntity.convertToCreditCard(creditCardDTO);
        creditCardRepository.save(creditCard);
        return convertEntityToDTO.convertToCreditCardDTO(creditCard);
    }
    /**
     * Get credit card.
     *
     * @param id the id
     * @param status the credit card status
     * @return the credit card dto
     */
    @Override
    public CreditCardDTO updateStatus(int id, String status) {
        CreditCard creditCard = getCreditCard(id);
        if(creditCard != null){
            creditCard.setStatus(CreditCardStatus.valueOf(status));
            creditCardRepository.save(creditCard);
        }
        return convertEntityToDTO.convertToCreditCardDTO(creditCard);
    }
    /**
     * Get credit card.
     *
     * @param transactionDTO the transaction
     * @return the credit card dto
     */
    @Override
    public CreditCardDTO deposit(CreditCardTransactionDTO transactionDTO) {
        CreditCard creditCard = getCreditCard(transactionDTO.getCreditCardId());
        BigDecimal amountConsumed = creditCard.getAmountConsumed();
        BigDecimal amountTransaction = transactionDTO.getAmount();

        if(amountTransaction.compareTo(amountConsumed) == 1){
            throw new BadRequestException("El monto despositado es mayor a la deuda.");
        }

        creditCard.setAmountConsumed(amountConsumed.subtract(amountTransaction));
        Transaction newTransaction = convertDTOToEntity.convertToTransaction(transactionDTO);
        newTransaction.setAction(TransactionAction.DEPOSIT);
        newTransaction.setCreditCard(creditCard);
        creditCardRepository.save(creditCard);
        transactionService.create(newTransaction);

        return convertEntityToDTO.convertToCreditCardDTO(creditCard);
    }
    /**
     * Get credit card.
     *
     * @param transactionDTO the transaction
     * @return the credit card dto
     */
    @Override
    public CreditCardDTO withdrawal(CreditCardTransactionDTO transactionDTO) {
        CreditCard creditCard = getCreditCard(transactionDTO.getCreditCardId());
        BigDecimal amountConsumed = creditCard.getAmountConsumed();
        BigDecimal amountAvailable = creditCard.getCreditLimit()
                .subtract(amountConsumed);
        BigDecimal amountTransaction = transactionDTO.getAmount();

        if(amountTransaction.compareTo(amountAvailable) == 1){
            throw new BadRequestException("No tiene saldo suficiente.");
        }

        creditCard.setAmountConsumed(amountConsumed.add(amountTransaction));
        Transaction newTransaction = convertDTOToEntity.convertToTransaction(transactionDTO);
        CreditCardDTO creditCardDTO = convertEntityToDTO.convertToCreditCardDTO(creditCard);
        newTransaction.setAction(TransactionAction.WITHDRAWAL);
        newTransaction.setCreditCard(creditCard);
        creditCardRepository.save(creditCard);
        transactionService.create(newTransaction);
        return creditCardDTO;
    }
    /**
     * Get credit card.
     *
     * @param id the id
     * @return the credit card
     */
    public CreditCard getCreditCard(int id){
        return creditCardRepository.findById(id).orElse(null);
    }
}
