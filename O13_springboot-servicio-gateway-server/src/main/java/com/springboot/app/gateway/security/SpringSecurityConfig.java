package com.springboot.app.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

//CLASE110: 
//@EnableWebfluxSecurity: PARA HABILITAR LA SEGURIDAD EN WEBFLUX.
//ESTA CLASE DE CONFIGURACIÓN NO IMPLEMENTA NADA, SOLO TENDRÁ UN MÉTODO BEAN QUE REGISTRA UN COMPONENTE DE TIPO SECURITY WEB FILTER CHAIN, PARA CONFIGURAR TODO LO QUE ES SEGURIDAD.

//1.-CONFIGURACIÓN DE LA SEGURIDAD DE LAS RUTAS. http.authorizeExchange().anyExchange().authenticated()... : SE PARTE CON TODO PROTEGIDO.
	//1.1.-.csrf().disable(): SE DESHABILITA EL TOKEN PARA VISTA DE FORMULARIOS CON HTML.

//CLASE111: SE LE DAN PERMISOS A LAS RUTAS..
	//2.- pathMatchers("/api/security/oauth/**").permitAll() : RUTAS DE OAUTH PÚBLICAS.
	//2.1.-MÁS RUTAS PÚBLICAS.
	//2.2.-RUTAS PARA ADMIN Y USER
	//2.3.-TODAS LAS DEMÁS RUTAS QUE QUEDAN, QUE SON "PUT, DELETE, CREATE" SOLO PARA ADMIN.

//CLASE115: SE CONFIGURA EL FILTRO DE AUTENTICACION DE LA CLASE JwtAuthenticationFilter.
	//3.-REGISTRO DEL PINCHE FILTRO DE AUTHENTICACION.
	//.addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) : SE LE PASA EL FILTRO INYECTADO Y EL ORDEN DEL FILTRO.

@EnableWebFluxSecurity
public class SpringSecurityConfig {

	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) {
		
		//1
		return http.authorizeExchange()
				//2
				.pathMatchers("/api/security/oauth/**").permitAll() 
				//2.1
				.pathMatchers(HttpMethod.GET,"/api/productos/listar",
						"/api/items/listar", 
						"/api/usuarios/usuarios", 
						"/api/items/ver/{id}/cantidad/{cantidad}",
						"/api/productos/ver/{id}").permitAll()
				//2.2
				.pathMatchers(HttpMethod.GET, "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN","USER")
				//2.3
				.pathMatchers("/api/productos/**", "/api/items/**", "/api/usuarios/usuarios/**").hasRole("ADMIN")
				
				.anyExchange().authenticated()
				.and()
				//3
				.addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				//1.1
				.csrf().disable()
				.build();
	}
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
}
