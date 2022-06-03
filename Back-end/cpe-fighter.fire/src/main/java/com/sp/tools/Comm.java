package com.sp.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.DTO.FireDTO;



public class Comm {
	
	private static final String URL_FIRE="http://vps.cpe-sn.fr:8081";  
	
	public static FireDTO getRemoteFire(int id) {

		RestTemplate restTemplate = new RestTemplate();

		// Send request with GET method and default Headers.
		FireDTO c_result = restTemplate.getForObject(URL_FIRE+"/"+id, FireDTO.class);
		return c_result;
	}
		

	public static void postRemoteFire(FireDTO fire) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<FireDTO> requestBody = new HttpEntity<>(fire, headers);

		// Send request with PUT method.
		restTemplate.postForEntity(URL_FIRE+"/"+ fire.getId(), requestBody,FireDTO.class);
	}
	
	
	
	
}