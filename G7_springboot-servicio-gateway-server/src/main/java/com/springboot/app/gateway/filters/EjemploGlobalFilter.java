package com.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

//1.-
	//exchange : CON ESTE OBJ SE PUEDE ACCEDER AL REQUEST Y AL RESPONSE. SE PUEDE MODIFICAR EL REQUEST Y EL RESPONSE. O REALIZAR VALIDACIONES. TAMBN SEGÚN LOS PARÁMETROS QUE SE MANDEN EN LAS CABECERAS, SE PUEDE DAR O NEGAR EL ACCESO A LOS SERVICIOS.
	//chain : UNE EN CADENA EL FILTRO PRE CON EL POST. DESPUÉS DE chain.filter(exchange) SIGUE EL FILTRO POST.
	//THEN() : DENTRO DEL THEN() VA TODO EL POST FILTER. LA RESPUESTA.
	//MONO : OBJ REALTIVO DEL TIPO MONO
		//Mono.fromRunnable(()..: PERMITE CREAR UN OBJ REACTIVO O MONOVOID QUE HARÁ ALGO EN LA RESPUESTA.
//2.-SE LE AGREGA UNA COOKIE CON EL COLOR ROJO A LA RESPUESTA.
	//build(): CONSTRUYE EL OBJETO COOKIE.
//3.-MODIFICACIÓN DEL CONTENT-TYPE A TEXTO PLANO.
//4.-MODIFICACIÓN DEL REQUEST CON FILTRO PRE
//5.-SE OBTIENE LA CABECERA DESDE EL REQUEST, PARA PODER VERLA EN EL RESPONSE.
	//getFirst(): PARA OBTENER UNA CABECERA EN PARTICULAR. EN ESTE CASO EL TOKEN.
	//SE PREGUNTA SI ES QUE EXISTE, PARA PASAR EL TOKEN A LA CABECERA DE LA RESPUESTA. PARA ESTO, TODO SE ANIDA DENTRO DE "Optional.ofNullable".  DESPUÉS, SI ES QUE ESTÁ PRESENTE,  SE USA "ifPresent()". DENTRO DE ESTE MÉTODO SE MODIFICA LA RESPUESTA CON UNA LABDA. ASIGNANDO EL VALOR DEL TOKEN CON CLAVE Y VALOR.
@Component
public class EjemploGlobalFilter implements GlobalFilter, Ordered {
	
	//1
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("Ejecutando filtro PRE");
		
		//4
		exchange.getRequest().mutate().headers(h->h.add("token", "123456"));
		
		//1.1
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			logger.info("ejecutando filtro post");
			
			//5
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
				exchange.getResponse().getHeaders().add("token", valor);
			});
			//2
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			//3
			//exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}
	
	private final Logger  logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);

	
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}
}
