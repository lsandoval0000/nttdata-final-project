package com.nttdata.bankaccountsavingsservice.controller;

import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountDto;
import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDataDto;
import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.service.SavingsAccountService;
import com.nttdata.bankaccountsavingsservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


/**
 * Savings account controller.
 */
@RestController
@RequestMapping("/api/bank-account/savings")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;
    private final TransactionService transactionService;

    /**
     * New savings account.
     *
     * @param newSavingsAccountRequestDto the new savings account
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<SavingsAccountResponseDto> newSavingsAccount(
            @Valid @RequestBody NewSavingsAccountRequestDto newSavingsAccountRequestDto
    ) {
        SavingsAccountDto savingsAccountDto = savingsAccountService.newSavingsAccount(newSavingsAccountRequestDto);
        SavingsAccountResponseDto response = SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDto)
                .message("La cuenta de ahorro ha sido creada satisfactoriamente.")
                .build();
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{dni}/balance")
                .buildAndExpand(newSavingsAccountRequestDto.getDni())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    /**
     * Deposit money into account.
     *
     * @param dni                    the dni
     * @param depositMoneyRequestDto the deposit money request dto
     * @return the response entity
     */
    @PostMapping("/{dni}/deposit")
    public ResponseEntity<SavingsAccountResponseDto> depositMoneyIntoAccount(
            @PathVariable String dni,
            @Valid @RequestBody DepositMoneyRequestDto depositMoneyRequestDto
    ) {
        SavingsAccountResponseDto savingsAccountResponseDto =
                savingsAccountService.depositMoneyIntoAccount(dni, depositMoneyRequestDto);
        return ResponseEntity.ok().body(savingsAccountResponseDto);
    }

    /**
     * Withdraw money from account.
     *
     * @param dni                     the dni
     * @param withdrawMoneyRequestDto the withdrawal money request dto
     * @return the response entity
     */
    @PostMapping("/{dni}/withdrawal")
    public ResponseEntity<SavingsAccountResponseDto> withdrawMoneyFromAccount(
            @PathVariable String dni,
            @Valid @RequestBody WithdrawMoneyRequestDto withdrawMoneyRequestDto
    ) {
        SavingsAccountResponseDto savingsAccountResponseDto =
                savingsAccountService.withdrawMoneyFromAccount(dni, withdrawMoneyRequestDto);
        return ResponseEntity.ok().body(savingsAccountResponseDto);
    }

    /**
     * Pay using account.
     *
     * @param dni         the dni
     * @param paymentInfo the payment info
     * @return the response entity
     */
    @PostMapping("/{dni}/paid")
    public ResponseEntity<SavingsAccountResponseDto> payUsingAccount(
            @PathVariable String dni,
            @Valid @RequestBody PaymentInfoDto paymentInfo
    ) {
        SavingsAccountResponseDto savingsAccountResponseDto = savingsAccountService.payUsingAccount(dni, paymentInfo);
        return ResponseEntity.ok().body(savingsAccountResponseDto);
    }

    /**
     * Gets account balance.
     *
     * @param dni the dni
     * @return the account balance
     */
    @GetMapping("/{dni}/balance")
    public ResponseEntity<SavingsAccountResponseDto> getAccountBalance(@PathVariable String dni) {
        SavingsAccountResponseDto accountBalance = savingsAccountService.getAccountBalance(dni);
        return ResponseEntity.ok().body(accountBalance);
    }

    /**
     * Gets account transaction history.
     *
     * @param dni the dni
     * @return the account transaction history
     */
    @GetMapping("/{dni}/transactions")
    public ResponseEntity<TransactionDataDto> getAccountTransactionHistory(
            @PathVariable String dni,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        TransactionDataDto accountTransactionHistory =
                transactionService.getAccountTransactionHistory(dni, page, pageSize);
        return ResponseEntity.ok().body(accountTransactionHistory);
    }
}
