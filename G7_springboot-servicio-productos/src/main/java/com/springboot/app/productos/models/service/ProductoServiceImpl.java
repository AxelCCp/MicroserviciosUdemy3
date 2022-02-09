package com.springboot.app.productos.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.productos.models.dao.IProductoDao;
import com.springboot.app.productos.models.entity.Producto;

@Service
public class ProductoServiceImpl implements IProductoService {

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		return (List<Producto>) productoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return productoDao.findById(id).orElse(null);
	}
	
	//CLASE67
	@Override
	@Transactional
	public Producto save(Producto producto) {
		return productoDao.save(producto);
	}
	
	//CLASE67
	@Override
	@Transactional
	public void deleteById(Long id) {
		productoDao.deleteById(id);
	}
	
	
	@Autowired
	private IProductoDao productoDao;

	

}
