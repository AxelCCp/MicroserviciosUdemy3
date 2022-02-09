package com.springboot.app.gateway.filters.factory;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

//CLASE DE FILTROS PERSONALIZADOS

//1.- GatewayFilter :  ES UNA INTERFAZ FUNCIONAL DE UN SOLO MÉTODO. EL METODO FILTER QUE DEVUELVE UN MONO<VOID> Y RECIVE POR PARÁMETRO EL EXCHANGE Y EL CHAIN.  Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);
//2.- CON UNA EXPRESIÓN LAMBDA SE IMPLEMENTA EL MÉTODO FILTER, TOMANDO SUS PARÁMETROS PARA LAMBDA.
//3.- SE SIGUE CON LA CADENA HACIA EL POST FILTER, USANDO UNA LAMBDA SIN PARÁMETRO.
@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {

	//PARA HACER UN MENSAJE Y UNA COOKIE PERSONALIZADA.
	public static class Configuracion {
		
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
	}

	
	public EjemploGatewayFilterFactory() {
		super(Configuracion.class);
		// TODO Auto-generated constructor stub
	}
	
	
	//1
	@Override
	public GatewayFilter apply(Configuracion config) {
		//2
		return (exchange, chain) ->{
			logger.info("ejecutando PRE gateway filter factory: " + config.mensaje);
			//3
			return  chain.filter(exchange).then(Mono.fromRunnable(()-> {
				
				Optional.ofNullable(config.cookieValor).ifPresent(cookie->{
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
				});
				
				logger.info("ejecutando POST gateway filter factory: " + config.mensaje);
			}));
		};
	}
	private final Logger logger = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);
}
