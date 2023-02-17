package com.nttdata.creditcard.util;

import com.nttdata.creditcard.dto.CreditCardDTO;
import com.nttdata.creditcard.dto.TransactionDTO;
import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.entity.Transaction;
import org.springframework.stereotype.Component;

/**
 * The type Convert entity to dto.
 */
@Component("convertEntityToDTO")
public class ConvertEntityToDTO {
    /**
     * Convert transaction to transaction dto.
     *
     * @param transaction the transaction
     * @return the transaction dto
     */
    public TransactionDTO  convertToTransactionDTO(Transaction transaction){
        if(transaction != null){
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setAction(String.valueOf(transaction.getAction()));
            transactionDTO.setDescription(transaction.getDescription());
            transactionDTO.setAmount(transaction.getAmount());
            transactionDTO.setDate(transaction.getDate());
            return  transactionDTO;
        }
        return null;
    }

    /**
     * Convert to credit card to credit card dto.
     *
     * @param creditCard the credit card
     * @return the credit card dto
     */
    public CreditCardDTO convertToCreditCardDTO(CreditCard creditCard){
        if(creditCard != null){
            CreditCardDTO creditCardDTO = new CreditCardDTO();
            creditCardDTO.setId(creditCard.getId());
            creditCardDTO.setAccountNumber(creditCard.getAccountNumber());
            creditCardDTO.setCardNumber(creditCard.getCardNumber());
            creditCardDTO.setCreditLimit(creditCard.getCreditLimit());
            creditCardDTO.setAmountConsumed(creditCard.getAmountConsumed());
            creditCardDTO.setStatus(creditCard.getStatus());
            creditCardDTO.setDni(creditCard.getDni());
            return  creditCardDTO;
        }
        return null;
    }
}
