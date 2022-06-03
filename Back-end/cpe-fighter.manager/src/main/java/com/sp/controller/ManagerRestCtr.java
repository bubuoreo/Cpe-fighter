package com.sp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.service.ManagerService;

@RestController
public class ManagerRestCtr {
	
	@Autowired
	ManagerService managerService;
	
	@GetMapping("/manager")
	public List<Object> manage() {
		List<Object> returnList = managerService.manageFire();
		// TODO : fin du feu --> retour à la caserne
		return returnList;
	}
	
}
