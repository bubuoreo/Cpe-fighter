package com.sp.service;


import java.util.*;

import com.DTO.FireDTO;
import com.sp.model.Fire;
import com.sp.repository.FireRepository;
import com.sp.tools.Comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FireService {
   
	@Autowired
    FireRepository fireRepository;
	
	private FireDTO FireToFireDTO(Fire fire){
		FireDTO fireDto = new FireDTO(fire.getId(),fire.getType(),fire.getIntensity(),fire.getRange(),fire.getLon(),fire.getLat());
		return fireDto;
	}
	
	/*
	private Fire FireDTOToFire(FireDTO fireDTO){
		Fire fire = new Fire(fireDTO.getId(),fireDTO.getType(),fireDTO.getIntensity(),fireDTO.getRange(),fireDTO.getLon(),fireDTO.getLat());
		return fire;
	}
	*/
	
    
    public List<FireDTO> getFires() {
		List<FireDTO> firesList = Comm.getFires();
		return firesList;
	}
    
    public FireDTO getFire(Integer id) {
		FireDTO fire = Comm.getFire(id);
		return fire;
	}

	
}
	
	