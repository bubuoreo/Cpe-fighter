package com.sp.controller;

import com.DTO.FireDTO;
import com.sp.service.FireService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FireRestController {

	@Autowired
	FireService fireService;
	
	@GetMapping("/fire/{id}")
	public FireDTO getFire(@PathVariable int id){
		return this.fireService.getFire(id);
	}
	
	@GetMapping("/fire")
	public Iterable<FireDTO> getFires(){
		return this.fireService.getFires();
	}

	
}
