package com.springboot.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.app.commons.usuarios.models.entity.Usuario;

@FeignClient(name="servicio-usuarios")
public interface IUsuarioFeignClient {

	
	@GetMapping("/usuarios/search/buscar-username")
	public Usuario findByUsername(@RequestParam("username")String username);
	
	//CLASE105
	@PutMapping("/usuarios/{id}")
	public Usuario update(@RequestBody Usuario usuario, @PathVariable Long id);
	
}
