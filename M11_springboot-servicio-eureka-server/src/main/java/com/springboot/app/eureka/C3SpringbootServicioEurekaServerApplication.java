package com.springboot.app.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@EnableEurekaServer
@SpringBootApplication
public class C3SpringbootServicioEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(C3SpringbootServicioEurekaServerApplication.class, args);
	}

}
