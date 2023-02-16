package com.nttdata.bankaccountsavingsservice.service;

import com.nttdata.bankaccountsavingsservice.controller.exception.MaxValueAllowedReachedException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NoSuchElementFoundException;
import com.nttdata.bankaccountsavingsservice.controller.exception.NotEnoughFundsException;
import com.nttdata.bankaccountsavingsservice.controller.exception.UnsupportedClientTypeException;
import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountResponseDto;
import com.nttdata.bankaccountsavingsservice.dto.deposit.DepositMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.mapper.SavingsAccountDtoMapper;
import com.nttdata.bankaccountsavingsservice.dto.newaccount.NewSavingsAccountRequestDto;
import com.nttdata.bankaccountsavingsservice.dto.payment.PaymentInfoDto;
import com.nttdata.bankaccountsavingsservice.dto.withdraw.WithdrawMoneyRequestDto;
import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import com.nttdata.bankaccountsavingsservice.repository.SavingsAccountRepository;
import com.nttdata.bankaccountsavingsservice.repository.TransactionRepository;
import com.nttdata.bankaccountsavingsservice.service.implementation.SavingsAccountServiceImpl;
import com.nttdata.bankaccountsavingsservice.util.AccountNumberGenerator;
import com.nttdata.bankaccountsavingsservice.util.ClientType;
import com.nttdata.bankaccountsavingsservice.util.DefaultValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SavingsAccountServiceTest {

    @Mock
    private SavingsAccountRepository savingsAccountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    private SavingsAccountDtoMapper savingsAccountDtoMapper;
    private SavingsAccountService underTest;

    @BeforeEach
    void setup() {
        savingsAccountDtoMapper = new SavingsAccountDtoMapper();
        underTest = new SavingsAccountServiceImpl(
                savingsAccountRepository,
                transactionRepository,
                savingsAccountDtoMapper);
    }

    @Test
    @DisplayName("Debe permitir al cliente aperturar una cuenta de ahorros")
    void shouldLetClientGetNewSavingsAccount() {
        NewSavingsAccountRequestDto newSavingsAccountRequestDto = NewSavingsAccountRequestDto
                .builder()
                .clientType(ClientType.PERSONAL.toString())
                .dni("47081541")
                .initialAmount(BigDecimal.TEN)
                .build();

        SavingsAccount account = SavingsAccount
                .builder()
                .accountNumber(AccountNumberGenerator.generateAccountNumber())
                .dni(newSavingsAccountRequestDto.getDni())
                .balance(newSavingsAccountRequestDto.getInitialAmount())
                .maintenanceFee(DefaultValues.MAINTENANCE_FEE)
                .monthlyAvailableMovements(DefaultValues.MONTHLY_AVAILABLE_MOVEMENTS)
                .build();

        given(savingsAccountRepository.countSavingsAccountByDni(newSavingsAccountRequestDto.getDni()))
                .willReturn(0L);
        given(savingsAccountRepository.save(any())).willReturn(account);

        SavingsAccountResponseDto response = underTest.newSavingsAccount(newSavingsAccountRequestDto);

        assertEquals(newSavingsAccountRequestDto.getInitialAmount(), response.getSavingsAccount().getBalance());
        assertTrue(response.getMessage().contains("creada"));
    }

    @Test
    @DisplayName("Debe generar la excepción UnsupportedClientTypeException si el tipo de cliente no es válido cuando" +
            " trata de aperturar una nueva cuenta de ahorros")
    void shouldRaiseUnsupportedClientTypeExceptionWhenClientTypeIsNotValidWhenTryingToCreateNewSavingsAccount() {
        NewSavingsAccountRequestDto newSavingsAccountRequestDto = NewSavingsAccountRequestDto
                .builder()
                .clientType(ClientType.EMPRESARIAL.toString())
                .build();

        assertThrows(UnsupportedClientTypeException.class,
                () -> underTest.newSavingsAccount(newSavingsAccountRequestDto));
        verify(savingsAccountRepository, never()).countSavingsAccountByDni(any());
        verify(savingsAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar la excepción MaxValueAllowedReachedException si el cliente ya posee el número máximo" +
            " de cuentas de ahorro permitidas")
    void shouldRaiseMaxValueAllowedReachedExceptionWhenClientTypeIsNotValidWhenTryingToCreateNewSavingsAccount() {
        NewSavingsAccountRequestDto newSavingsAccountRequestDto = NewSavingsAccountRequestDto
                .builder()
                .clientType(ClientType.PERSONAL.toString())
                .dni("47081541")
                .build();

        given(savingsAccountRepository.countSavingsAccountByDni(newSavingsAccountRequestDto.getDni()))
                .willReturn(1L);

        assertThrows(MaxValueAllowedReachedException.class,
                () -> underTest.newSavingsAccount(newSavingsAccountRequestDto));
        verify(savingsAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe permitir al cliente depositar dinero en su cuenta de ahorros")
    void shouldLetClientDepositMoneyIntoAccount() {
        String dni = "47081541";
        DepositMoneyRequestDto depositMoneyRequestDto = DepositMoneyRequestDto
                .builder()
                .amount(BigDecimal.TEN)
                .build();
        SavingsAccount savingsAccount = SavingsAccount
                .builder()
                .dni(dni)
                .balance(BigDecimal.TEN)
                .build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.of(savingsAccount));
        given(savingsAccountRepository.save(savingsAccount)).willReturn(savingsAccount);

        SavingsAccountResponseDto response = underTest.depositMoneyIntoAccount(dni, depositMoneyRequestDto);

        assertEquals(BigDecimal.valueOf(20L), response.getSavingsAccount().getBalance());
    }

    @Test
    @DisplayName("Debe generar la excepción NoSuchElementFoundException cuando el cliente trata de depositar dinero" +
            " en una cuenta de ahorros que no existe")
    void shouldRaiseNoSuchElementFoundExceptionWhenClientTryDepositMoneyIntoNonExistingSavingsAccount() {
        String dni = "47081541";
        DepositMoneyRequestDto depositMoneyRequestDto = DepositMoneyRequestDto.builder().build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class,
                () -> underTest.depositMoneyIntoAccount(dni, depositMoneyRequestDto));
    }

    @Test
    @DisplayName("Debe permitir al cliente retirar dinero de la cuenta de ahorros")
    void shouldLetClientWithdrawMoneyFromAccount() {
        String dni = "47081541";
        WithdrawMoneyRequestDto withdrawMoneyRequestDto = WithdrawMoneyRequestDto
                .builder()
                .amount(BigDecimal.TEN)
                .build();
        SavingsAccount savingsAccount = SavingsAccount
                .builder()
                .dni(dni)
                .balance(BigDecimal.valueOf(1000L))
                .build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.of(savingsAccount));
        given(transactionRepository
                .countAllWithdrawTransactionsOfCurrentMonthBySavingsAccount(any()))
                .willReturn(10);
        given(savingsAccountRepository.save(savingsAccount)).willReturn(savingsAccount);

        SavingsAccountResponseDto response = underTest.withdrawMoneyFromAccount(dni, withdrawMoneyRequestDto);

        assertTrue(response.getMessage().contains("Retiro"));
        assertEquals(BigDecimal.valueOf(986.0D), response.getSavingsAccount().getBalance());
    }

    @Test
    @DisplayName("Debe generar NoSuchElementFoundException cuando el cliente trata de retirar dinero de una " +
            "cuenta de ahorros que no existe")
    void shouldRaiseNoSuchElementFoundExceptionWhenClientTryToWithdrawMoneyFromNonExistingSavingsAccount() {
        String dni = "47081541";
        WithdrawMoneyRequestDto withdrawMoneyRequestDto = WithdrawMoneyRequestDto.builder().build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class,
                () -> underTest.withdrawMoneyFromAccount(dni, withdrawMoneyRequestDto));
        verify(transactionRepository, never()).countAllWithdrawTransactionsOfCurrentMonthBySavingsAccount(any());
        verify(savingsAccountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar NotEnoughFundsException cuando el cliente trata de retirar dinero de una cuenta de " +
            "ahorros pero no tiene fondos suficientes")
    void shouldRaiseNotEnoughFundsExceptionWhenClientTryToWithdrawMoneyFromSavingsAccountButDoesNotHaveEnoughFunds() {
        String dni = "47081541";
        WithdrawMoneyRequestDto withdrawMoneyRequestDto = WithdrawMoneyRequestDto
                .builder()
                .amount(BigDecimal.TEN)
                .build();
        SavingsAccount savingsAccount = SavingsAccount
                .builder()
                .dni(dni)
                .balance(BigDecimal.TEN)
                .build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.of(savingsAccount));
        given(transactionRepository
                .countAllWithdrawTransactionsOfCurrentMonthBySavingsAccount(any()))
                .willReturn(10);

        assertThrows(NotEnoughFundsException.class,
                () -> underTest.withdrawMoneyFromAccount(dni, withdrawMoneyRequestDto));
        verify(savingsAccountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe permitir al cliente paga un servicio con su cuenta de ahorros")
    void shouldLetClientPayUsingSavingsAccount() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(500L))
                .build();
        SavingsAccount savingsAccount = SavingsAccount
                .builder()
                .dni(dni)
                .balance(BigDecimal.valueOf(500L))
                .build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.of(savingsAccount));
        given(savingsAccountRepository.save(savingsAccount)).willReturn(savingsAccount);

        SavingsAccountResponseDto response = underTest.payUsingAccount(dni, paymentInfo);

        assertEquals(BigDecimal.ZERO, response.getSavingsAccount().getBalance());
        assertTrue(response.getMessage().contains("Pago"));
    }

    @Test
    @DisplayName("Debe generar NoSuchElementFoundException cuando el cliente trata de pagar un servicio con una " +
            "cuenta de ahorros que no existe")
    void shouldRaiseNoSuchElementFoundExceptionWhenClientTryToPayWithNonExistingSavingsAccount() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto.builder().build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class,
                () -> underTest.payUsingAccount(dni, paymentInfo));
        verify(savingsAccountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar NotEnoughFundsException cuando el cliente trata de pagar un servicio con una cuenta de " +
            "ahorros pero no tiene fondos suficientes")
    void shouldRaiseNotEnoughFundsExceptionWhenClientTryToPayWithSavingsAccountButDoesNotHaveEnoughFunds() {
        String dni = "47081541";
        PaymentInfoDto paymentInfo = PaymentInfoDto
                .builder()
                .amountToPay(BigDecimal.valueOf(500L))
                .build();
        SavingsAccount savingsAccount = SavingsAccount
                .builder()
                .dni(dni)
                .balance(BigDecimal.TEN)
                .build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.of(savingsAccount));

        assertThrows(NotEnoughFundsException.class,
                () -> underTest.payUsingAccount(dni, paymentInfo));
        verify(savingsAccountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar el balance de la cuenta de ahorros del cliente")
    void shouldGetAccountBalance() {
        String dni = "47081541";
        SavingsAccount savingsAccountEntity = SavingsAccount.builder().balance(BigDecimal.TEN).build();

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.of(savingsAccountEntity));

        SavingsAccountResponseDto savingsAccountBalance = underTest.getAccountBalance(dni);

        assertEquals(savingsAccountEntity.getBalance(), savingsAccountBalance.getSavingsAccount().getBalance());
    }

    @Test
    @DisplayName("Debe generar la excepción NoSuchElementFoundException cuando el cliente no posee una cuenta de ahorros" +
            " y trata de obtener el balance")
    void shouldRaiseNoSuchElementFoundExceptionWhenTryingToGetBalanceFromNonExistingSavingsAccount() {
        String dni = "47081541";

        given(savingsAccountRepository.findSavingsAccountByDni(dni)).willReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> underTest.getAccountBalance(dni));
    }
}