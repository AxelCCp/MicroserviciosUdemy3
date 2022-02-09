package com.springboot.app.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.springboot.app.item.clientes.ProductoClienteRestFeign;
import com.springboot.app.item.models.Item;

@Service("serviceFeign")
public class ItemServiceFeign implements IItemService {

	@Override
	public List<Item> findAll() {
		return clienteFeign.listar().stream().map(p-> new Item(p,1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		return  new Item(clienteFeign.detalle(id),cantidad);
	}

	@Autowired
	private ProductoClienteRestFeign clienteFeign;
}
