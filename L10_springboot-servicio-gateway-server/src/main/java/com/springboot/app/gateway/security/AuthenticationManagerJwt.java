package com.springboot.app.gateway.security;

import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

//CLASE113: CLASE DE UN ADMINISTRADOR DE AUTENTICACIÓN
//1.1.-EL MÉTODO DEVLEVE UN FLUJO Y CONTIENE UN ELEMENTO "Authentication", Y RECIVE POR PARÁMENTRO RECIBE OTRO Authentication QUE DEBE CONTENER EL TOKEN. Y ES ESTE Authentication QUE CONTIENE AL TOKEN, EL QUE SE PASA POR EL FILTRO. 
//1.2.-just(): CONVIERTE  A UN OBJ NORMAL EN UN OBJETO REACTIVO.
	//Mono.just(authentication.getCredentials().toString()) : SE OBTIENE EL TOKEN "authentication.getCredentials()" Y SE PASA A STRING "toString()".
	//map(): LUEGO DE PROCESA EL TOKEN Y SE OBTIENEN LOS CLAIMS. LOS CLAIMS ES LA INFORMACIÓN QUE CONTIENE EL TOKEN PARA LA AUTENTICACIÓN.
	//SecretKey llave... :SE CREA LA CONEXIÓN DE LA LLAVE SECRETA PARA OBTENER INFO DEL TOKEN. SE CONECTA CON EL SERVIDR DE CONFIGURACIONES QUE ESTÁ EN GITHUB.
	//hmacShaKeyFor() : ESTE MÉTODO RECIBE UN OBJ DE TIPO BYTES. POR LO TANTO SE USA EL MÉTODO getBytes().
	//CODIFICACIÓN A BASE64 :  
		//Base64.getEncoder().encode();
		//SE HACE ESTO PARA QUE LA LLAVE SEA MÁS SEGURA. 
		//EN EL SERVICIO OAUTH2 HAY QUE TAMBIEN HAY QUE IMPLEMENTAR BASE64. EN LA CLASE AuthorizationServerConfig.
	//VALIDACIÓN DEL TOKEN Y DEVOLVER AL FLUJO LOS CLAIMS
		//Jwts.parserBuilder(): PARA OBTENER DATOS Y VALIDAR.
		//parseClaimsJws(TOKEN) : MÉTODO PARA EXTRAER LOS CLAIMS DEL TOKEN. Y CON getBody() SE OBTIENEN.
	//MAP() : EL FLUJO ESTÁ CON EL TIPO CLAIMS Y HAY QUE PASARLO AL TIPO FINAL QUE ES Authentication. PARA ESTO SE USA OTRO MAP().
		//DENTRO DE ESTE MAP SE APROVECHAN DE OBTENER LOS CLAIMS().
			//username : claims.get("user_name") : USERNAME ES EL NOMBRE DEL ATRIBUTO EN EL TOKEN, Y EN ESTE MÉTODO GET SE USA SEPARANDOLO CON UN GUIÓN. "String.class" :SE HACE REFERENCIA AL TIPO DE DATOS QUE RECIVE EL GET().
			//List<String> roles : SE OBTIENEN LOS ROLES. HAY QUE RECORDAR QUE JSON MANEJA A LOS ROLES BAJO EL NOMBRE DE LOS AUTHORITIES.
			//SE CONVIERTE A TIPO DE DATO DE LOS ROLES QUE MANEJA SPRING SECURITY. RECORDAR QUE LOS MANEJA COMO UNA COLECCION DE GRANTED AUTHORITIES.
	//UsernamePasswordAuthenticationToken(username,null,authorities): SE DEVUELVE EL OBJ DEL TIPO Mono<Authentication>.
@Component
public class AuthenticationManagerJwt implements ReactiveAuthenticationManager{

	//1.1  //EL MÉTODO COMPARA EL TOKEN QUE SE LE PASA POR PARÁMETRO A TRAVÉS DEL authentication, CON EL TOKEN DEL SERVICIO-CONFIG-SERVER EN GITHUB POR MEDIO DE LA KEY.
	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		// TODO Auto-generated method stub
		return Mono.just(authentication.getCredentials().toString())
				.map(token->{
					SecretKey llave = Keys.hmacShaKeyFor(Base64.getEncoder().encode(llaveJwt.getBytes()));
					
					return Jwts.parserBuilder().setSigningKey(llave).build().parseClaimsJws(token).getBody();
				})
				.map(claims -> {
					String username = claims.get("user_name", String.class);
					
					List<String> roles = claims.get("authorities", List.class);
					Collection<GrantedAuthority>authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
							.collect(Collectors.toList());
					return new UsernamePasswordAuthenticationToken(username,null,authorities);
				});
	}
	
	@Value("${config.security.oauth.jwt.key}")
	private String llaveJwt;

}
