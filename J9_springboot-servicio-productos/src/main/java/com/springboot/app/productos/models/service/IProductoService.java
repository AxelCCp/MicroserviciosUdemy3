package com.springboot.app.productos.models.service;

import java.util.List;

import com.springboot.app.commons.models.entity.Producto;

public interface IProductoService {
	
	public List<Producto>findAll();
	public Producto findById(Long id);
	
	//CLASE 67
	public Producto save(Producto producto);
	//CLASE 67
	public void deleteById(Long id);
}
