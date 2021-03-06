package com.springboot.app.usuarios.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.springboot.app.usuarios.models.entity.Usuario;

//CLASE85: @RepositoryRestResource



@RepositoryRestResource(path="usuarios")
public interface IUsuarioDao extends PagingAndSortingRepository<Usuario, Long> {

	//CLASE84  //CLASE86 localhost:8090/api/usuarios/usuarios/search/findByUsername?username=ZZZZ           localhost:8090/api/usuarios/usuarios/search/buscar-username?nombre=RRRR
	@RestResource(path="buscar-username")
	public Usuario findByUsername(@Param("username")String username);
	
	//CLASE84
	@Query("select u from Usuario u where u.username=?1")
	public Usuario obtenerPorUsername(String username);
}
