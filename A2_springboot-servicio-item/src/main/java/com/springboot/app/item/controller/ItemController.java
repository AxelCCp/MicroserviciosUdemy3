package com.springboot.app.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.item.models.Item;
import com.springboot.app.item.models.service.IItemService;

@RestController
public class ItemController {
	
	
	@GetMapping("listar")
	public List<Item>listar(){
		return itemService.findAll();
		
	}
	
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
		
	}
	
	
	@Autowired
	@Qualifier("serviceFeign")
	private IItemService itemService; 
	
}
