package com.nttdata.bankaccountsavingsservice.service.implementation;

import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountDto;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDetailDto;
import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.service.SavingsAccountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Savings account service.
 */
@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {
    /**
     * @param newSavingsAccountRequestDto
     * @return SavingsAccountDto
     */
    @Override
    public SavingsAccountDto newSavingsAccount(NewSavingsAccountRequestDto newSavingsAccountRequestDto) {
        return null;
    }

    /**
     * @param dni
     * @param depositMoneyRequestDto
     * @return SavingsAccountResponseDto
     */
    @Override
    public SavingsAccountResponseDto depositMoneyIntoAccount(String dni, DepositMoneyRequestDto depositMoneyRequestDto) {
        return null;
    }

    /**
     * @param dni
     * @param withdrawMoneyRequestDto
     * @return SavingsAccountResponseDto
     */
    @Override
    public SavingsAccountResponseDto withdrawMoneyFromAccount(String dni, WithdrawMoneyRequestDto withdrawMoneyRequestDto) {
        return null;
    }

    /**
     * @param dni
     * @param paymentInfo
     * @return SavingsAccountResponseDto
     */
    @Override
    public SavingsAccountResponseDto payUsingAccount(String dni, PaymentInfoDto paymentInfo) {
        return null;
    }

    /**
     * @param dni
     * @return SavingsAccountResponseDto
     */
    @Override
    public SavingsAccountResponseDto getAccountBalance(String dni) {
        return null;
    }

    /**
     * @param dni
     * @return List<TransactionDetailDto>
     */
    @Override
    public List<TransactionDetailDto> getAccountTransactionHistory(String dni) {
        return new ArrayList<>();
    }
}
