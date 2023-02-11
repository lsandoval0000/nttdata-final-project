package com.nttdata.creditspersonalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CreditsPersonalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditsPersonalServiceApplication.class, args);
	}

}
