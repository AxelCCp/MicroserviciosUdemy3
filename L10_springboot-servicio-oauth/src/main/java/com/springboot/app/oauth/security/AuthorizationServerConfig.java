package com.springboot.app.oauth.security;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//CLASE94: ESTE ES EL SERVIDOR DE AUTORIZACIÓN

//1.-//REGISTRAR/REGISTRAR EL AuthenticationManager EN EL AuthorizationServer. TAMBN EL TOKENSTORE Y EL TOKEN CONVERTER QUE GUARDA LOS DATOS DEL USUARIO EN EL TOKEN.
	//1.1.-REGISTRO DEL AUTHENTICATION MANAGER.
	//1.2.-CONFIGURACION DE TOKEN CONVERTER. // SE PONE @BEAN, YA Q SERÁ UN COMPONENTE DE SPRING PARA LA CONFIGURACIÓN.
	//1.3.-CÓDIGO SECRETO PARA VALIDAR EL TOKEN, PARA CUANDO EL CLIENTE QUIERA ACCEDER A LOS SERVICIOS.
	//1.4.-SE GENERA TOKENSTORE, EL QUE GUARDA EL TOKEN CON LOS DATOS DEL TOKEN CONVERTER.

//CLASE95
	//1.5.-CONFIGURACIÓN DE LOS CLIENTES DEL FRONTEND. 
	//"frontendapp": ES UN IDENTIFICADOR DE LA APP. 
	//secret(passwordEncoder.encode("12345"): ES LA CONTRASEÑA QUE SE TIENE QUE ENCRIPTAR.

	//clients.inMemory().withClient("frontendapp")   ESTOS SON LOS DATOS DE AUTENTICACIÓN DE LA APPLICATION DEL CLIENTE.
	//.secret(passwordEncoder.encode("12345"))

	//scopes("read","write"): ALCANCES DE LA APPLICACIÓN.
	//authorizedGrantTypes("password", "refresh_token"): SE SEÑALA QUE SE OBTENDRÁ EL TOKEN A TRAVÉS DEL PASSWORD. CON REFRESH SE RENOVARÁ EL TOKEN. 
	//accessTokenValiditySeconds(3600) : TIEMPO DE VALIDEZ DEL TOKEN.

	//1.6: PERMISO QUE VAN A TENER LOS ENDPOINTS DEL SERVIDOR DE AUTHORIZATION DE OAUTH2, PARA GENERAR Y VALIDAR EL TOKEN.
	//permitAll() : PERMISO DE SPRING SECURITY.
	//checkTokenAccess("isAuthenticated()"): checkTokenAccess() : RUTA PARA VALIDAR TOKEN. isAuthenticated() :MÉTODO DE SPRING SECURITY Q PERMITE VERIFICAR QUE EL CLIENTE ESTÁ AUTENTICADO.


//CLASE97
	//2.-SE UNA LA INFORMACIÓN ADICIONAL A LA INFORMACIÓN  POR DEFECTO DEL ACCESSTOKENCONVERTER. PARA ESTO SE UNA TOKENENHANCERCHAIN, UNA CLASE QUE FORMA UNA CADENA PARA UNIR LA INFO.
	//2.1.-SE AGREGA LA CADENA A LA CONFIGURACION DEL ENDPOINT

//CLASE100
	//3.- SE USA EL SERVICIO CONFIG SERVER, PARA LEER LA CONFIGURACION DESDE GITHUB

//CLASE113
	//4.- SE CODIFICA LA KEY A BASE64.
		//encodeToString(): RECIBE BYTES, POR LO TANTO SE USA TAMBIÉN  getBytes().
		//setSigningKey(): RECIBE UN STRING, POR LO TANTO SE USA encodeToString().
		//TAMBIÉN SE HACE EN ZUUL.

@RefreshScope //3.4
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}
	
	//1.5
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(env.getProperty("config.security.oauth.client.id")) //3.1
		.secret(passwordEncoder.encode(env.getProperty("config.security.oauth.client.secret")))  //3.2
		.scopes("read","write")
		.authorizedGrantTypes("password", "refresh_token")
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);
	}
	
	//1
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		//2
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain(); 
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken,accessTokenConverter()));
		
		//1.1     
		endpoints.authenticationManager(authenticationManager)
		//1.4
		.tokenStore(tokenStore())
		//1.2
		.accessTokenConverter(accessTokenConverter())
		//2.1
		.tokenEnhancer(tokenEnhancerChain);
	}
	
	//1.2
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter	= new JwtAccessTokenConverter();
		//1.3    //4
		tokenConverter.setSigningKey(Base64.getEncoder().encodeToString(env.getProperty("config.security.oauth.jwt.key").getBytes())); //3.3
		return tokenConverter;
	}
	
	//1.4
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	@Autowired  //3
	private Environment env;
	
}
