package com.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DTO.FireDTO;
import com.DTO.VehicleDTO;
import com.sp.repository.VehicleRepository;
import com.sp.tools.Comm;

@Service
public class VehicleService {
	
	@Autowired
	VehicleRepository vehicleRepository;
	
	public List<VehicleDTO> getVehicles() {
		List<VehicleDTO> vehiclesList = Comm.getVehicles();
		return vehiclesList;
	}

	public List<FireDTO> getFires() {
		List<FireDTO> firesList = Comm.getFires();
		return firesList;
	}
	
}
