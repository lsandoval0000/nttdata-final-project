package com.nttdata.creditcard.controller;

import com.nttdata.creditcard.dto.TransactionDTO;
import com.nttdata.creditcard.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Transaction controller.
 */
@RestController
@RequestMapping(value = "/api/credit-card-transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * List transactions list.
     *
     * @param accountNumber the account number
     * @return the transactions by account
     */
    @GetMapping("/account-transactions/{accountNumber}")
    public List<TransactionDTO> listTransactions(@PathVariable("accountNumber") String accountNumber){
        return transactionService.transactionsByAccount(accountNumber);
    }

}
