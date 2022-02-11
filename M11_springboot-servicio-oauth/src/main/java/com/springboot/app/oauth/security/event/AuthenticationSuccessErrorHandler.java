package com.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.services.IUsuarioService;

import feign.FeignException;

//CLASE103: CLASE PARA MANEJAR EXITO O ERROR EN EL LOGIN

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher{

	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		
		//CLASE104
		if(authentication.getName().equalsIgnoreCase("frontendapp")) {
			return;
		}
		
		UserDetails user = (UserDetails) authentication.getPrincipal();
		System.out.println("Success login: " + user.getUsername());
		log.info("Success login: " + user.getUsername());
		
		//CLASE106...
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		if(usuario.getIntentos() != null && usuario.getIntentos() > 0) {
			usuario.setIntentos(0); //SE DEJA EL CONTADOR EN 0
			usuarioService.update(usuario, usuario.getId());
		}//...CLASE106
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		log.error("Error en el login: " + exception.getMessage());
		System.out.println("Error en el login: " + exception.getMessage());
		
		//CLASE106...
		try {
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			if(usuario.getIntentos()==null) {
				usuario.setIntentos(0);
			}
			log.info("Intentos actual es de: " + usuario.getIntentos());
			usuario.setIntentos(usuario.getIntentos()+1);
			log.info("Intentos después es de: " + usuario.getIntentos());
			
			if(usuario.getIntentos()>=3) {
				log.error(String.format("Usuario %s deshabilitado por superar máximo de intentos.", usuario.getUsername()));
				usuario.setEnabled(false);
			}
			
			usuarioService.update(usuario, usuario.getId());
			
		}catch(FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
		}//...CLASE106
	}

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	//CLASE105
	@Autowired
	private IUsuarioService usuarioService;
	
	
}
