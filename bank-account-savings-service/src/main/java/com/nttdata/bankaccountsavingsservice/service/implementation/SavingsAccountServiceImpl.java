package com.nttdata.bankaccountsavingsservice.service.implementation;

import com.nttdata.bankaccountsavingsservice.controller.exception.MaxValueAllowedReachedException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NotEnoughFundsException;
import com.nttdata.bankaccountsavingsservice.controller.exception.UnsupportedClientTypeException;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountDto;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.mapper.SavingsAccountDtoMapper;
import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.entity.Transaction;
import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import com.nttdata.bankaccountsavingsservice.repository.TransactionRepository;
import com.nttdata.bankaccountsavingsservice.repository.SavingsAccountRepository;
import com.nttdata.bankaccountsavingsservice.service.SavingsAccountService;
import com.nttdata.bankaccountsavingsservice.service.api.ClientServiceClient;
import com.nttdata.bankaccountsavingsservice.util.AccountNumberGenerator;
import com.nttdata.bankaccountsavingsservice.util.ClientType;
import com.nttdata.bankaccountsavingsservice.util.DefaultValues;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Savings account service.
 */
@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final TransactionRepository transactionRepository;
    private final ClientServiceClient clientServiceClient;
    private final SavingsAccountDtoMapper savingsAccountDtoMapper;


    /**
     * @param newSavingsAccountRequestDto
     * @return SavingsAccountDto
     */
    @Transactional
    @Override
    public SavingsAccountDto newSavingsAccount(NewSavingsAccountRequestDto newSavingsAccountRequestDto) {
        if (newSavingsAccountRequestDto.getClientType().equals(ClientType.EMPRESARIAL.toString())) {
            throw new UnsupportedClientTypeException("Los tipos de cliente empresarial no pueden crear cuentas de ahorro.");
        }
        if (!clientServiceClient.clientExists(newSavingsAccountRequestDto.getDni())) {
            throw new NoSuchElementFoundException("El cliente no existe.");
        }
        if (savingsAccountRepository.countSavingsAccountByDni(newSavingsAccountRequestDto.getDni()) > 0) {
            throw new MaxValueAllowedReachedException("Solo se puede poseer una cuenta por cliente como máximo.");
        }
        SavingsAccount account = SavingsAccount
                .builder()
                .accountNumber(AccountNumberGenerator.generateAccountNumber())
                .dni(newSavingsAccountRequestDto.getDni())
                .balance(newSavingsAccountRequestDto.getInitialAmount())
                .maintenanceFee(DefaultValues.MAINTENANCE_FEE)
                .monthlyAvailableMovements(DefaultValues.MONTHLY_AVAILABLE_MOVEMENTS)
                .build();
        SavingsAccount savedAccount = savingsAccountRepository.save(account);
        Transaction transaction = Transaction
                .builder()
                .amount(savedAccount.getBalance())
                .savingsAccount(savedAccount)
                .transaction("Se crea cuenta de ahorro para el cliente con DNI : " + savedAccount.getDni())
                .build();
        transactionRepository.save(transaction);
        return savingsAccountDtoMapper.convertToDto(savedAccount);
    }

    /**
     * @param dni
     * @param depositMoneyRequestDto
     * @return SavingsAccountResponseDto
     */
    @Transactional
    @Override
    public SavingsAccountResponseDto depositMoneyIntoAccount(String dni, DepositMoneyRequestDto depositMoneyRequestDto) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);
        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }
        BigDecimal newBalance = savingsAccount.getBalance().add(depositMoneyRequestDto.getAmount());
        savingsAccount.setBalance(newBalance);
        SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);
        Transaction transaction = Transaction
                .builder()
                .amount(savedAccount.getBalance())
                .savingsAccount(savedAccount)
                .transaction(
                        "El cliente con DNI: "
                                + savedAccount.getDni()
                                + " deposita "
                                + depositMoneyRequestDto.getAmount().toString()
                                + ", el nuevo balance de la cuenta es "
                                + savedAccount.getBalance().toString()
                )
                .build();
        transactionRepository.save(transaction);
        return SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDtoMapper.convertToDto(savedAccount))
                .message("Depósito realizado exitosamente")
                .build();
    }

    /**
     * @param dni
     * @param withdrawMoneyRequestDto
     * @return SavingsAccountResponseDto
     */
    @Transactional
    @Override
    public SavingsAccountResponseDto withdrawMoneyFromAccount(String dni, WithdrawMoneyRequestDto withdrawMoneyRequestDto) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);
        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }
        BigDecimal newBalance = savingsAccount.getBalance().subtract(withdrawMoneyRequestDto.getAmount());
        if (newBalance.compareTo(BigDecimal.valueOf(0L)) < 0) {
            throw new NotEnoughFundsException("El monto que el cliente desea retirar excede sus ahorros");
        }
        savingsAccount.setBalance(newBalance);
        // TODO missing movement limit validation and fee
        SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);
        Transaction transaction = Transaction
                .builder()
                .amount(savedAccount.getBalance())
                .savingsAccount(savedAccount)
                .transaction(
                        "El cliente con DNI: "
                                + savedAccount.getDni()
                                + " retira "
                                + withdrawMoneyRequestDto.getAmount().toString()
                                + ", el nuevo balance de la cuenta es "
                                + savedAccount.getBalance().toString()
                )
                .build();
        transactionRepository.save(transaction);
        return SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDtoMapper.convertToDto(savedAccount))
                .message("Retiro realizado exitosamente")
                .build();
    }

    /**
     * @param dni
     * @param paymentInfo
     * @return SavingsAccountResponseDto
     */
    @Transactional
    @Override
    public SavingsAccountResponseDto payUsingAccount(String dni, PaymentInfoDto paymentInfo) {
        // TODO missing implementation
        return null;
    }

    /**
     * @param dni
     * @return SavingsAccountResponseDto
     */
    @Override
    public SavingsAccountResponseDto getAccountBalance(String dni) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByDni(dni).orElse(null);
        if (savingsAccount == null) {
            throw new NoSuchElementFoundException("El cliente no posee una cuenta de ahorros.");
        }
        return SavingsAccountResponseDto
                .builder()
                .savingsAccount(savingsAccountDtoMapper.convertToDto(savingsAccount))
                .message("Consulta realizada exitosamente")
                .build();
    }
}
