package com.nttdata.creditspersonalservice.service;

import com.nttdata.creditspersonalservice.dto.PersonalCreditResponseDto;
import com.nttdata.creditspersonalservice.dto.newcredit.NewPersonalCreditRequestDto;
import com.nttdata.creditspersonalservice.dto.payment.PaymentInfoDto;

/**
 * The personal credit service.
 */
public interface PersonalCreditService {
    /**
     * Request personal credit.
     *
     * @param newPersonalCreditRequestDto the new personal credit request dto
     * @return the personal credit response dto
     */
    PersonalCreditResponseDto requestCredit(NewPersonalCreditRequestDto newPersonalCreditRequestDto);

    /**
     * Pay personal credit.
     *
     * @param dni         the dni
     * @param paymentInfo the payment info
     * @return the personal credit response dto
     */
    PersonalCreditResponseDto payCredit(String dni, PaymentInfoDto paymentInfo);

    /**
     * Gets credit balance.
     *
     * @param dni the dni
     * @return the credit balance
     */
    PersonalCreditResponseDto getCreditBalance(String dni);
}
