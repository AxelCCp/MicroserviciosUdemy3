package com.springboot.app.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.springboot.app.commons.models.entity"}) //PARA QUE EL PROYECTO PUEDA ESCANEAR EL PACKAGE DONDE ESTÁ LA CLASE "PRODUCTO"  EN EL MICROSERVICIO COMMONS.
public class SpringbootServicioProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioProductosApplication.class, args);
	}

}