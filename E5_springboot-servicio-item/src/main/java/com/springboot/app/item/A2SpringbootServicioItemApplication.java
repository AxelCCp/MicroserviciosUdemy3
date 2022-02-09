package com.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


//@EnableCircuitBreaker //SE HABILITA HISTRYX  ...EN LA CLASE 43 SE QUITA HYSTRIX PARA USAR RESILIENCE4J
@EnableEurekaClient
@EnableFeignClients 
@SpringBootApplication
public class A2SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(A2SpringbootServicioItemApplication.class, args);
	}

}
