package com.springboot.app.item;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	
	@Bean("clienteRest")
	@LoadBalanced      //PARA BALANCEO DE CARGA CON RESTTEMPLATE.
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}
}
