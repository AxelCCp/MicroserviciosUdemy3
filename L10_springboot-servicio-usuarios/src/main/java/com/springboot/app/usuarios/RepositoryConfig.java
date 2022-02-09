package com.springboot.app.usuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.springboot.app.commons.usuarios.models.entity.Role;
import com.springboot.app.commons.usuarios.models.entity.Usuario;


//CLASE87
	//config: PARA CONFIGURAR EL REPOSITORIO REST.
	//cors: 
	//CON ESTE MÉTODO SE MUESTRAN LOS ID DE ROLE Y USUARIO EN EL JSON, AL HACER LAS QUERYS DE LA interface IUsuarioDao.


@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer{

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		config.exposeIdsFor(Usuario.class, Role.class);
	}

	
	
}
