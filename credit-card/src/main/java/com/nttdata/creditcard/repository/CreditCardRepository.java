package com.nttdata.creditcard.repository;

import com.nttdata.creditcard.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Credit card repository.
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    /**
     * Find by dni list.
     *
     * @param dni the dni
     * @return the list
     */
    public List<CreditCard> findByDni(String dni);

    /**
     * Find by account number credit card.
     *
     * @param accountNumber the account number
     * @return the credit card
     */
    public CreditCard findByAccountNumber(String accountNumber);
}
