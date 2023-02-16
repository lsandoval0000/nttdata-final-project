package com.nttdata.bankaccountsavingsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
}
