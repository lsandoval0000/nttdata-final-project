package com.nttdata.bankaccountsavingsservice.service;

import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;


/**
 * Savings account service.
 */
public interface SavingsAccountService {
    /**
     * New savings account.
     *
     * @param newSavingsAccountRequestDto the new savings account request dto
     * @return the savings account dto
     */
    SavingsAccountResponseDto newSavingsAccount(NewSavingsAccountRequestDto newSavingsAccountRequestDto);

    /**
     * Deposit money into savings account.
     *
     * @param dni                    the dni
     * @param depositMoneyRequestDto the deposit money request dto
     * @return the savings account response dto
     */
    SavingsAccountResponseDto depositMoneyIntoAccount(String dni, DepositMoneyRequestDto depositMoneyRequestDto);

    /**
     * Withdraw money from savings account.
     *
     * @param dni                     the dni
     * @param withdrawMoneyRequestDto the withdrawal money request dto
     * @return the savings account response dto
     */
    SavingsAccountResponseDto withdrawMoneyFromAccount(String dni, WithdrawMoneyRequestDto withdrawMoneyRequestDto);

    /**
     * Pay using savings account.
     *
     * @param dni         the dni
     * @param paymentInfo the payment info
     * @return the savings account response dto
     */
    SavingsAccountResponseDto payUsingAccount(String dni, PaymentInfoDto paymentInfo);

    /**
     * Gets account balance.
     *
     * @param dni the dni
     * @return the account balance
     */
    SavingsAccountResponseDto getAccountBalance(String dni);
}
