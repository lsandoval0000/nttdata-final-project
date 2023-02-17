package com.nttdata.creditcard.controller;

import com.nttdata.creditcard.dto.CreditCardDTO;
import com.nttdata.creditcard.dto.CreditCardTransactionDTO;
import com.nttdata.creditcard.dto.NewCreditCardDTO;
import com.nttdata.creditcard.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The Credit card controller.
 */
@RestController
@RequestMapping(value="/api/credit-card")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    /**
     * Create credit card.
     *
     * @param creditCardDTO the credit card dto
     * @return the credit card dto
     */
    @PostMapping(value="/new")
    public ResponseEntity<CreditCardDTO> create(@RequestBody NewCreditCardDTO creditCardDTO){
        return new ResponseEntity<>(creditCardService.create(creditCardDTO), HttpStatus.CREATED);
    }

    /**
     * Credit cards by dni list.
     *
     * @param dni the dni
     * @return the list
     */
    @GetMapping(value="/user/{dni}")
    public ResponseEntity<List<CreditCardDTO>> creditCardsByDni(@PathVariable String dni) {
        return new ResponseEntity<>(creditCardService.creditCardsByDni(dni), HttpStatus.OK);
    }

    /**
     * Update status credit card.
     *
     * @param id     the id
     * @param status the status
     * @return the credit card dto
     */
    @PutMapping(value="/update-status/{id}/{status}")
    public ResponseEntity<CreditCardDTO> updateStatus(@PathVariable("id") int id, @PathVariable("status") String status){
        return new ResponseEntity<>(creditCardService.updateStatus(id, status), HttpStatus.OK);
    }

    /**
     * Deposit credit card.
     *
     * @param creditCardTransactionDTO the credit card transaction dto
     * @return the credit card dto
     */
    @PutMapping(value="/deposit")
    public ResponseEntity<CreditCardDTO> deposit(@RequestBody CreditCardTransactionDTO creditCardTransactionDTO){
        return new ResponseEntity<>(creditCardService.deposit(creditCardTransactionDTO), HttpStatus.OK);
    }

    /**
     * Withdrawal credit card.
     *
     * @param creditCardTransactionDTO the credit card transaction dto
     * @return the credit card dto
     */
    @PutMapping(value="/withdrawal")
    public ResponseEntity<CreditCardDTO> withdrawal(@RequestBody CreditCardTransactionDTO creditCardTransactionDTO){
        return new ResponseEntity<>(creditCardService.withdrawal(creditCardTransactionDTO), HttpStatus.OK);
    }

}
