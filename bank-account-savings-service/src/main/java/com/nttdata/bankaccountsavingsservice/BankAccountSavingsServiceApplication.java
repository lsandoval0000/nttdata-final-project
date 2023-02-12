package com.nttdata.bankaccountsavingsservice;

import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import com.nttdata.bankaccountsavingsservice.repository.SavingsAccountRepository;
import com.nttdata.bankaccountsavingsservice.util.AccountNumberGenerator;
import com.nttdata.bankaccountsavingsservice.util.DefaultValues;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

/**
 * The bank account savings service application.
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class BankAccountSavingsServiceApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BankAccountSavingsServiceApplication.class, args);
    }

    /**
     * Load data command line runner.
     *
     * @return the command line runner
     */
    @Bean
    public CommandLineRunner loadData(SavingsAccountRepository savingsAccountRepository) {
        return args -> {
            for (int counter = 0; counter < 10; counter++) {
                SavingsAccount account = SavingsAccount
                        .builder()
                        .dni((String.valueOf(counter)).repeat(8))
                        .accountNumber(AccountNumberGenerator.generateAccountNumber())
                        .balance(BigDecimal.valueOf((counter + 10 * 500)))
                        .maintenanceFee(DefaultValues.MAINTENANCE_FEE)
                        .monthlyAvailableMovements(DefaultValues.MONTHLY_AVAILABLE_MOVEMENTS)
                        .build();
                savingsAccountRepository.save(account);
            }
        };
    }
}
