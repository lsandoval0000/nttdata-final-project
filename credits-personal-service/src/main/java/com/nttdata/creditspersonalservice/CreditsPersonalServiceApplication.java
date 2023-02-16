package com.nttdata.creditspersonalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The Credits personal service application.
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class CreditsPersonalServiceApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CreditsPersonalServiceApplication.class, args);
	}
}
