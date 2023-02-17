package com.nttdata.creditcard.service;

import com.nttdata.creditcard.dto.CreditCardDTO;
import com.nttdata.creditcard.dto.CreditCardTransactionDTO;
import com.nttdata.creditcard.dto.NewCreditCardDTO;
import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.entity.Transaction;
import com.nttdata.creditcard.util.CreditCardStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface Credit card service.
 */
public interface CreditCardService {
    /**
     * Credit cards by dni list.
     *
     * @param dni the dni
     * @return the list
     */
    public List<CreditCardDTO> creditCardsByDni(String dni);

    /**
     * Create credit card dto.
     *
     * @param creditCard the credit card
     * @return the credit card dto
     */
    public CreditCardDTO create(NewCreditCardDTO creditCard);

    /**
     * Update status credit card dto.
     *
     * @param id     the id
     * @param status the status
     * @return the credit card dto
     */
    public CreditCardDTO updateStatus(int id, String status);

    /**
     * Deposit credit card dto.
     *
     * @param creditCardTransactionDTO the credit card transaction dto
     * @return the credit card dto
     */
    public CreditCardDTO deposit(CreditCardTransactionDTO creditCardTransactionDTO);

    /**
     * Withdrawal credit card dto.
     *
     * @param creditCardTransactionDTO the credit card transaction dto
     * @return the credit card dto
     */
    public CreditCardDTO withdrawal(CreditCardTransactionDTO creditCardTransactionDTO);
}
