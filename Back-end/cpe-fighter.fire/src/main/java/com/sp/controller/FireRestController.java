package com.sp.controller;

import com.DTO.FireDTO;
//import com.sp.requests.LoginRequest;
//import com.sp.requests.RegisterRequest;
import com.sp.service.FireService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FireRestController {

	@Autowired
	FireService fireService;

	
	@GetMapping("/fire/{id}")
	public FireDTO getFire(@PathVariable int id){
		return this.fireService.getFireById(id);
	}
	
	@GetMapping("/fire")
	public Iterable<FireDTO> getFires(){
		return this.fireService.getAllFires();
	}

	
}
