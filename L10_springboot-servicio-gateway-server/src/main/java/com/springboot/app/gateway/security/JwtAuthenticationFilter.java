package com.springboot.app.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

//CLASE114: ESTA ES LA CLASE DE FILTRO PARA LA AUTENTICACIÓN.
//exchange : SE PUEDE OBTENER EL TOKEN Y EL REQUEST QUE NOS ESTÁN ENVIANDO DESDE ALGÚN CLIENTE EN LAS CABECERAS. 

//1.-
	//justOrEmpty(null):SE CONVIERTE A UN FLUJO QUE PUEDE CONTENER EL TOKEN COMO TAMBIÉN PUEDE ESTAR VACÍO. POR LO TANTO TAMBIEN PUEDE MANDAR UN FLUJO VACÍO.
	//exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION): SE OBTIENE EL TOKEN, LA CABECERA, Y CON GETFIRST() SE OBTIENE EL NOBMRE DE LA CABECERA HTTP
	//RESUMIENDO: SE OBTIENE UN FLUJO CON LA CABECERA HTTP.
//2.-
	//SE PROCESA EL FLUJO CON LA CABECERA HTTP. 
    //filter(): LAS CABECERAS "AUTHORIZATION" COMIENZAN CON "BEARER" Y CON filter() SE PUEDE PREGUNTAR SI EXISTE EL "BEARER" CON < authHeather -> authHeather.startsWith("Bearer ") > ... SI HAY BEARER HAY TOKEN.
//3.-
	//SI NO HAY BEARER SE CREA UN FLUJO VACÍO QUE NO HACE NADA.
	//switchIfEmpty(): CON SWITCH SI LLEGA A ESTAR VACÍO, DEVUELVE UN Mono.empty(). Y EL PROGRAMA SE SALE DEL FLUJO DEL MÉTODO.
//4.-
	//map(token -> token.replace("Bearer ", "")) : SE PROCESA LA CABECERA, QUITANDOLE EL BEARER CON MAP(). CON ESTO SOLO QUEDA EL TOKEN.
//5.-
	//YA SE TIENE EL TOKEN FORMATEADO. AHORA HAY QUE VALIDARLO CON EL AuthenticationManager. LO QUE SE ESTÁ PASANDO EN LOS () DE FLATMAP() ES UN FLUJO "Mono<Authentication> authenticate()" , POR ESO SE USA FLATMAP() Y NO MAP().
	//authenticate() : DENTRO DE ESTE METODO SE CREA UN OBJ "AUTHENTICATION" CON LA IMPLEMENTACIÓN DE UsernamePasswordAuthenticationToken.
//6.-
	//flatMap(): SE PARECE AL MAP(). MAP() DEVUELVE UN OBJ CORRIENTE COMO UN STRING, MIENTRAS QUE FLATMAP() DEVUELVE OTRO FLUJO, MONO O FLUX. O SEA, SE TRANSFORMA UN FLUJO CON LOS ELEMENTOS DE OTRO FLUJO.
	//AQUÍ YA SE EMITE EL OBJ Authentication, YA QUE YA SE LLAMÓ AL MÉTODO authenticate() DE LA CLASE AuthenticationManagerJwt.
	//AHORA TOCA AUTENTICAR EN EL CONTEXTO...
	//chain.filter(): SE USA LA CADENA DE LOS FILTROS.
	//exchange. : PARA CONTINUAR CON LA EJECUCIÓN DE LOS DEMÁS FILTROS Y DEL REQUEST.
	//contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)): SE ASIGNA EL OBJ AUTHENTICATION AL CONTEXTO DE SPRING SECURITY Y DE LA APP.
	//ReactiveSecurityContextHolder: SE USA PARA GUARDAR LA AUTHETICATION EN EL CONTEXTO.
//7.- YA CON ESTO QUEDA AUTENTICADO. SOLO FALTA CONFIGURAR LA CLASE SpringSecurityConfig CON ESTE FILTRO

@Component
public class JwtAuthenticationFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		//1
		return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))	
				//2;
				.filter(authHeather -> authHeather.startsWith("Bearer "))
				//3
				.switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
				//4
				.map(token -> token.replace("Bearer ", ""))
				//5
				.flatMap(token -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(null,token)))
				//6
				.flatMap(authentication -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)));
	}
	
	@Autowired
	private ReactiveAuthenticationManager authenticationManager;
}
