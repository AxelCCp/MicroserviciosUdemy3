package com.springboot.app.oauth.services;

import com.springboot.app.commons.usuarios.models.entity.Usuario;

//CLASE 97: ESTA INTERFAZ SE USA PARA AGREGAR INFORMACIÓN ADICIONAL AL TOKEN.

public interface IUsuarioService {
	public Usuario findByUsername(String username);
}
