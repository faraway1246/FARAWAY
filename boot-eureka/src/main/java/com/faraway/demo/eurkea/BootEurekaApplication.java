package com.faraway.demo.eurkea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class BootEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootEurekaApplication.class, args);
	}

}
