package com.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.clients.IUsuarioFeignClient;

import brave.Tracer;


//CLASE92
	//UserDetailsService : SE ENCARGA DE AUTENTICAR Y OBTENER AL USUARIO POR EL USERNAME.
	//List <GrantedAuthority> : ALMACENA LISTA DE ROLES DEL TIPO SimpleGrantedAuthority.
	//peek(): EXPRESIÓN LAMBDA, PARA MOSTRAR EL NOMBRE DE CADA ROLE.

//CLASE97: SE IMPLEMENTA LA INTERFAZ IUsuarioService PARA AGREGAR INFORMACIÓN ADICIONAL AL TOKEN.

//CLASE134: SE AGREGAN MÁS ATRIBUTOS A LA TRAZA, PARA CUANDO NO SE ENCUENTREN LOS USUARIOS. SE AGREGA UN TAG.

@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = client.findByUsername(username);
		
		if(usuario==null) {
			
			String error = "Error en el login, no existe el usuario '" + username + "' en el sistema";
			
			log.error(error);
			tracer.currentSpan().tag("error.mensaje", error + " :  TAG" );
			throw new UsernameNotFoundException(error);
		}
		
		
		List <GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role->new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority->log.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("Usuario autenticado: " + username);
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
	
	
	//CLASE105
	@Override
	public Usuario update(Usuario usuario, Long id) {
		return client.update(usuario, id);
	}
	
	
	//CLASE97
	@Override
	public Usuario findByUsername(String username) {
		// TODO Auto-generated method stub
		return client.findByUsername(username);
	}
	
	
	@Autowired
	private IUsuarioFeignClient client;
	
	private Logger log = LoggerFactory.getLogger(UsuarioService.class);

	//CLASE134
	@Autowired
	private Tracer tracer;

	
}



