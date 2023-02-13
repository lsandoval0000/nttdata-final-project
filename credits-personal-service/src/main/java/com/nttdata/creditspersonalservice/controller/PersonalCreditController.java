package com.nttdata.creditspersonalservice.controller;

import com.nttdata.creditspersonalservice.dto.PersonalCreditResponseDto;
import com.nttdata.creditspersonalservice.dto.newcredit.NewPersonalCreditRequestDto;
import com.nttdata.creditspersonalservice.dto.payment.PaymentInfoDto;
import com.nttdata.creditspersonalservice.dto.transaction.TransactionDataDto;
import com.nttdata.creditspersonalservice.service.PersonalCreditService;
import com.nttdata.creditspersonalservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * The personal credit controller.
 */
@RestController
@RequestMapping("/api/credits/personal")
@RequiredArgsConstructor
public class PersonalCreditController {

    private final PersonalCreditService personalCreditService;
    private final TransactionService transactionService;

    /**
     * Request credit.
     *
     * @param newPersonalCreditRequestDto the new personal credit request dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<PersonalCreditResponseDto> requestCredit(
            @Valid @RequestBody NewPersonalCreditRequestDto newPersonalCreditRequestDto) {
        PersonalCreditResponseDto personalCredit = personalCreditService.requestCredit(newPersonalCreditRequestDto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{dni}/balance")
                .buildAndExpand(newPersonalCreditRequestDto.getDni())
                .toUri();
        return ResponseEntity.created(uri).body(personalCredit);
    }

    /**
     * Pay credit.
     *
     * @param dni         the dni
     * @param paymentInfo the payment info
     * @return the response entity
     */
    @PostMapping("/{dni}/paid")
    public ResponseEntity<PersonalCreditResponseDto> payCredit(
            @PathVariable String dni,
            @Valid @RequestBody PaymentInfoDto paymentInfo) {
        PersonalCreditResponseDto personalCredit = personalCreditService.payCredit(dni, paymentInfo);
        return ResponseEntity.ok().body(personalCredit);
    }

    /**
     * Gets credit balance.
     *
     * @param dni the dni
     * @return the credit balance
     */
    @GetMapping("/{dni}/balance")
    public ResponseEntity<PersonalCreditResponseDto> getCreditBalance(@PathVariable String dni) {
        PersonalCreditResponseDto personalCreditBalance = personalCreditService.getCreditBalance(dni);
        return ResponseEntity.ok().body(personalCreditBalance);
    }

    /**
     * Gets credit transaction history.
     *
     * @param dni      the dni
     * @param page     the page
     * @param pageSize the page size
     * @return the credit transaction history
     */
    @GetMapping("/{dni}/transactions")
    public ResponseEntity<TransactionDataDto> getCreditTransactionHistory(
            @PathVariable String dni,
            @RequestParam(defaultValue = "1", name = "page") Integer page,
            @RequestParam(defaultValue = "5", name = "page_size") Integer pageSize) {
        TransactionDataDto creditTransactionHistory =
                transactionService.getCreditTransactionHistory(dni, page - 1, pageSize);
        return ResponseEntity.ok().body(creditTransactionHistory);
    }
}
