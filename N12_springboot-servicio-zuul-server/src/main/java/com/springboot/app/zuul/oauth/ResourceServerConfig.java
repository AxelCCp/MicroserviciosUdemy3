package com.springboot.app.zuul.oauth;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//CLASE98: ESTE ES EL SERVIDOR DE RECURSOS QUE SE CONFIGURA EN ZUUL
//1.- METODOS HEREDADOS.
	//configure(HttpSecurity http): MÉTODO PARA PROTEGER RUTAS Y ENDPOINTS DE ZUUL SERVER.
		//antMatchers(): SE HACE REFERENCIA A LA RUTA.
		//permitAll(): PARA CONFIGURAR UNA RUTA COMO RUTA PÚBLICA.
		//hasAnyRole(): SOLO DETERMINADOS ROLES TIENEN ACCESO A LAS RUTAS.
	//configure(ResourceServerSecurityConfigurer resources): MÉTODO PARA CONFIGURAR EL TOKEN STORE.

//2.-SE COPIAN LOS BEANS DESDE EL MICROSERVICIO OAUTH

//3.-CLASE100: SE USA INYECCIÓN DE PROPIEDAD, YA QUE SE LEERÁ EL APPLICATION PROPERTIES DESDE GITHUB.

//CLASE102
//4.-CONFIGURACIÓN DEL CORS: PARA QUE LAS APPLICACIONES CLIENTE QUE ESTÉN EN OTROS DOMINIOS Y SERVIDORES, TENGAN ACCESO A LAS RUTAS DE LOS MICROSERVICIOS.
	//4.1.-CONFIGURACIÓN DE APPLICACIONES CLIENTE.
		//corsConfig.setAllowedOrigins(Arrays.asList("*")) : CON ESTO SE PERMITEN APLICACIONES DE CUALQUIER ORIGEN.
		//corsConfig.setAllowedMethods(Arrays.asList("*")) : CON ESTO SE PERMITEN TODOS LOS TIPOS DE MÉTODOS GET, PUT, DELETE, ETC.
	//4.2.-SE PASA LA CONFIGURACIÓN DEL CORSCONFIG A LAS RUTAS URL.
		// "/**"  : CON ESTO SE APLICA A TODAS LAS RUTAS.
	//4.3.-FILTRO DE CORS: SE CREA PARA QUE EL CORS NO QUEDE CONFIGURADO SOLO EN SPRING SECURIRY, SINO QUE TAMBIÉN QUEDE CONFIGURADO A NIVEL GLOBAL EN TODO SPRING.
	//4.4.-SE LE DA UNA PRIORIDAD ALTA.

//CLASE113
//5.-SE CODIFICA LA KEY A BASE64 PARA MAYOR SEGURIDAD.

@RefreshScope //3.2
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter  {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
		.antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/items/listar", "/api/usuarios/usuarios").permitAll()
		.antMatchers(HttpMethod.GET, "/api/productos/ver/{id}","/api/items/ver/{id}/cantidad/{cantidad}", "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.POST, "/api/productos/crear", "/api/items/crear", "/api/usuarios/usuarios").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/api/productos/editar/{id}", "/api/items/editar/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/api/productos/eliminar/{id}", "/api/items/eliminar/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
		//4
		.and().cors().configurationSource(corsConfigurationSource());
	}

	

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter	= new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(Base64.getEncoder().encodeToString(jwtKey.getBytes())); //3.1  //5
		return tokenConverter;
	}	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	//4
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		//4.1
		corsConfig.setAllowedOrigins(Arrays.asList("*"));
		corsConfig.setAllowedMethods(Arrays.asList("POST","GET","PUT","DELETE","OPTIONS"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		//4.2
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

	
	//4.3
	@Bean
	public FilterRegistrationBean<CorsFilter>corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE); //4.4
		return bean;
	}
	
	
	//3
	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;
}
