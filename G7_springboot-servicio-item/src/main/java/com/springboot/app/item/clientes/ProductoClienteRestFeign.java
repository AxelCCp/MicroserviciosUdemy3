package com.springboot.app.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.springboot.app.item.models.Producto;


@FeignClient(name="servicio-productos")
public interface ProductoClienteRestFeign {
	
	@GetMapping("/listar")
	public List<Producto>listar();
	
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id);
	
	
	//CLASE72
	@PostMapping("/crear")
	public Producto crear(@RequestBody Producto producto);
	
	//CLASE72
	@PutMapping("/editar/{id}")
	public Producto editar(@RequestBody Producto producto, @PathVariable Long id);
	
	//CLASE72
	@DeleteMapping("/eliminar/{id}")
	public void eliminar(@PathVariable Long id);
}
