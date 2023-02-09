package com.nttdata.bankaccountsavingsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bank-account/savings")
@RequiredArgsConstructor
public class SavingsAccountController {

    @PostMapping
    public ResponseEntity<String> newSavingsAccount() {
        return ResponseEntity.ok().body("");
    }

    @PostMapping("")
    public ResponseEntity<String> depositMoneyIntoAccount() {
        return ResponseEntity.ok().body("");
    }
}
