package com.sp.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.DTO.FireDTO;



public class Comm {
	
	private static final String URL_FIRE="http://vps.cpe-sn.fr:8081/fire";  
	
	public static List<FireDTO> getFires() {

		RestTemplate restTemplate = new RestTemplate();
		// Send request with GET method and default Headers.
		FireDTO[] fireDTOs = restTemplate.getForObject(URL_FIRE, FireDTO[].class);
		List<FireDTO> firesList = new ArrayList<FireDTO>();
		for (FireDTO fireDTO : fireDTOs) {
			firesList.add(fireDTO);
		}
		return firesList;
	}
	
	public static FireDTO getFire(Integer id) {

		RestTemplate restTemplate = new RestTemplate();
		// Send request with GET method and default Headers.
		FireDTO fireDTO = restTemplate.getForObject(URL_FIRE + "/" + id, FireDTO.class);
		return fireDTO;
	}
}