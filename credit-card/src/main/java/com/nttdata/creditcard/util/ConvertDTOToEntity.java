package com.nttdata.creditcard.util;

import com.nttdata.creditcard.dto.CreditCardTransactionDTO;
import com.nttdata.creditcard.dto.NewCreditCardDTO;
import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Convert dto to entity.
 */
@Component("convertDTOToEntity")
public class ConvertDTOToEntity {
    /**
     * Convert credit card DTO to credit card.
     *
     * @param creditCardDTO the credit card dto
     * @return the credit card
     */
    public CreditCard convertToCreditCard(NewCreditCardDTO creditCardDTO){
        if(creditCardDTO != null) {
            CreditCard creditCard = new CreditCard();
            creditCard.setCardNumber(creditCardDTO.getCardNumber());
            creditCard.setCreditLimit(creditCardDTO.getCreditLimit());
            creditCard.setAmountConsumed(BigDecimal.valueOf(0.00));
            creditCard.setDni(creditCardDTO.getDni());
            creditCard.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
            creditCard.setStatus(CreditCardStatus.ACTIVE);
            return creditCard;
        }
        return null;
    }

    /**
     * Convert transaction DTO to transaction.
     *
     * @param creditCardTransactionDTO the credit card transaction dto
     * @return the transaction
     */
    public Transaction convertToTransaction(CreditCardTransactionDTO creditCardTransactionDTO){
        if(creditCardTransactionDTO != null) {
            Transaction transaction = new Transaction();
            transaction.setDescription(creditCardTransactionDTO.getDescription());
            transaction.setAmount(creditCardTransactionDTO.getAmount());
            transaction.setDate((new SimpleDateFormat("dd-MM-yyyy")).format(new Date()));
            return transaction;
        }
        return null;
    }
}
