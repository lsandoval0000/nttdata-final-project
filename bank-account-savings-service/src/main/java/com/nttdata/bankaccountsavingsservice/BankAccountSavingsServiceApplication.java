package com.nttdata.bankaccountsavingsservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
     * @return the command line runner
     */
    @Bean
    public CommandLineRunner loadData() {
        return args -> {

        };
    }
}
