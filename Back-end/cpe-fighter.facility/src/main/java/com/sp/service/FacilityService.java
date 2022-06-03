package com.sp.service;

import java.util.List;

import com.DTO.FacilityDTO;
import com.sp.tools.Comm;

public class FacilityService {

	
    public List<FacilityDTO> getFacilitys() {
		List<FacilityDTO> FacilitysList = Comm.getFacilitys();
		return FacilitysList;
	}
    
    public FacilityDTO getFacility(Integer id) {
		FacilityDTO Facility = Comm.getFacility(id);
		return Facility;
	}

}
