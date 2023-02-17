package com.nttdata.currentaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CurrentAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrentAccountApplication.class, args);
	}

}
