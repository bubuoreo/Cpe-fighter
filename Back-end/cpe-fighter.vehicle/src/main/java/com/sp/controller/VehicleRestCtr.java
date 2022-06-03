package com.sp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.FireDTO;
import com.DTO.VehicleDTO;
import com.sp.model.VehicleService;
import com.sp.tools.Comm;

@RestController
public class VehicleRestCtr {
	
	@Autowired
	VehicleService vehicleService;
	
	@GetMapping("/manager")
	public List<Object> manage() {
		List<Object> returnList = vehicleService.manageFire();
		// récupération des feux
		// récup les véhicules
		// Si véhicule à la caserne, téléporte vers le feu
		// si véhicule pas à la caserne, c'est qu'il travaille sur un feu
		// fin du feu --> retour à la caserne
		return returnList;
	}
	
	@GetMapping("/manager/vehicle")
	public List<VehicleDTO> getVehicles() {
		System.out.println("Récupération des véhicules");
		List<VehicleDTO> vehiclesList = vehicleService.getVehicles();
		return vehiclesList;
	}
	
	@GetMapping("/manager/fire")
	public List<FireDTO> getFires() {
		System.out.println("Récupération des feux");
		List<FireDTO> firesList = vehicleService.getFires();
		return firesList;
	}

}