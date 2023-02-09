package com.nttdata.bankaccountsavingsservice;

import com.nttdata.bankaccountsavingsservice.model.GeneralSavingsAccountConfigEntity;
import com.nttdata.bankaccountsavingsservice.repository.GeneralSavingsAccountConfigRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

/**
 * The bank account savings service application.
 */
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
     * @param generalSavingsAccountConfigRepository the general savings account config repository
     * @return the command line runner
     */
    @Bean
    public CommandLineRunner loadData(
            GeneralSavingsAccountConfigRepository generalSavingsAccountConfigRepository
    ) {
        return args -> {
            GeneralSavingsAccountConfigEntity generalSavingsAccountConfig =
                    GeneralSavingsAccountConfigEntity
                            .builder()
                            .monthlyAvailableMovements(5)
                            .maintenanceFee(BigDecimal.valueOf(0L))
                            .feeForOperation(BigDecimal.valueOf(0.30D))
                            .build();
            generalSavingsAccountConfigRepository.save(generalSavingsAccountConfig);
        };
    }
}
