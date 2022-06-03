package com.sp.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.DTO.FacilityDTO;

public class Comm {
	
	private static final String URL_Facility="http://vps.cpe-sn.fr:8082/facility";  
	
	public static List<FacilityDTO> getFacilitys() {

		RestTemplate restTemplate = new RestTemplate();
		// Send request with GET method and default Headers.
		FacilityDTO[] FacilityDTOs = restTemplate.getForObject(URL_Facility, FacilityDTO[].class);
		List<FacilityDTO> FacilitysList = new ArrayList<FacilityDTO>();
		for (FacilityDTO FacilityDTO : FacilityDTOs) {
			FacilitysList.add(FacilityDTO);
		}
		return FacilitysList;
	}
	
	public static FacilityDTO getFacility(Integer id) {

		RestTemplate restTemplate = new RestTemplate();
		// Send request with GET method and default Headers.
		FacilityDTO FacilityDTO = restTemplate.getForObject(URL_Facility + "/" + id, FacilityDTO.class);
		return FacilityDTO;
	}
}