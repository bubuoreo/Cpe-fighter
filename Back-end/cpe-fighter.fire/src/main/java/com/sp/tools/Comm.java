package com.sp.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.DTO.FireDTO;



public class Comm {
	
	private static final String URL_FIRE="http://vps.cpe-sn.fr:8081";  
	
	public static Iterable<FireDTO> getRemoteFire() {

		RestTemplate restTemplate = new RestTemplate();

		// Send request with GET method and default Headers.
		Iterable<FireDTO> c_result = (Iterable<FireDTO>)restTemplate.getForObject(URL_FIRE+"/fire", FireDTO.class);
		return c_result;
	}
			
}