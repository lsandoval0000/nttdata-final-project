package com.nttdata.bankaccountsavingsservice.controller;

import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDetailDto;
import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/bank-account/savings")
@RequiredArgsConstructor
public class SavingsAccountController {

    @PostMapping
    public ResponseEntity<SavingsAccountResponseDto> newSavingsAccount(
            @Valid @RequestBody NewSavingsAccountRequestDto newSavingsAccountRequestDto
    ) {
        // TODO Implementation required
        SavingsAccountResponseDto response = SavingsAccountResponseDto.builder().build();
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{accountId}/balance")
                .buildAndExpand(response.getSavingsAccount().getAccountId())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<SavingsAccountResponseDto> depositMoneyIntoAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody DepositMoneyRequestDto depositMoneyRequestDto
    ) {
        // TODO Implementation required
        return ResponseEntity.ok().body(SavingsAccountResponseDto.builder().build());
    }

    @PostMapping("/{accountId}/withdrawal")
    public ResponseEntity<SavingsAccountResponseDto> withdrawMoneyFromAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody WithdrawMoneyRequestDto withdrawMoneyRequestDto
    ) {
        // TODO Implementation required
        return ResponseEntity.ok().body(SavingsAccountResponseDto.builder().build());
    }

    @PostMapping("/{accountId}/paid")
    public ResponseEntity<SavingsAccountResponseDto> payUsingAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody PaymentInfoDto paymentInfo
    ) {
        // TODO Implementation required
        return ResponseEntity.ok().body(SavingsAccountResponseDto.builder().build());
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<SavingsAccountResponseDto> getAccountBalance(@PathVariable Long accountId) {
        // TODO Implementation required
        return ResponseEntity.ok().body(SavingsAccountResponseDto.builder().build());
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDetailDto>> getAccountTransactionHistory(@PathVariable Long accountId) {
        // TODO Implementation required
        return ResponseEntity.ok().body(new ArrayList<>());
    }
}
