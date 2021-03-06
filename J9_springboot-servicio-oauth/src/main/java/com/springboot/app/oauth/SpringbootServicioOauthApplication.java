package com.springboot.app.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//CLASE96: SE IMPLEMENTA CommandLineRunner, PARA GENERAR LOS CÓDIGOS ENCRIPTADOS EN LA  CONSOLA.
//EN EL MÉTODO RUN() SE ENCRIPTAN LAS CONTRASEÑAS. SE GENERAN 4 CONTRASEÑAS ENCRIPTADAS A PARTIR DE 12345.

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class SpringbootServicioOauthApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioOauthApplication.class, args);	
	}

	@Override
	public void run(String... args) throws Exception {
		String password = "12345";
		for(int i=0; i<4; i++) {
			String passwordBcrypt = passwordEncode.encode(password);
			System.out.println(passwordBcrypt);
		}
		
	}


	
	@Autowired
	private BCryptPasswordEncoder passwordEncode;
	
}
