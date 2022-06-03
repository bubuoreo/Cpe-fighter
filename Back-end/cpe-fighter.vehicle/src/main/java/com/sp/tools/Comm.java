package com.sp.tools;

import org.springframework.web.client.RestTemplate;

import com.DTO.FireDTO;
import com.DTO.VehicleDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Comm {

	// "http://localhost:8081/fire";
	private static final String URL_FIRE = "http://vps.cpe-sn.fr:8081/fire";
	private static final String URL_API_VEHICLE = "http://vps.cpe-sn.fr:8081/vehicle";

	public static List<FireDTO> getFires() {
		
		System.out.println("requête pour obtenir les feux");
		RestTemplate restTemplate = new RestTemplate();
		// Send request with GET method and default Headers.
		FireDTO[] fireDTOs = restTemplate.getForObject(URL_FIRE, FireDTO[].class);
		List<FireDTO> firesList = new ArrayList<FireDTO>();
		for (FireDTO fireDTO : fireDTOs) {
			firesList.add(fireDTO);
		}
		return firesList;
	}

	public static List<VehicleDTO> getVehicles() {
		
		System.out.println("requête pour obtenir les vehicules");
		RestTemplate restTemplate = new RestTemplate();
		// Send request with GET method and default Headers.
		VehicleDTO[] vehicleDTOs= restTemplate.getForObject(URL_API_VEHICLE, VehicleDTO[].class);
		 List<VehicleDTO> vehiclesList = new ArrayList<VehicleDTO>();
		 for (VehicleDTO vehicleDTO : vehicleDTOs) {
			vehiclesList.add(vehicleDTO);
		}
		return vehiclesList;
	}

//	public static void postRemoteUser(UserDTO user) {
//
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//		HttpEntity<UserDTO> requestBody = new HttpEntity<>(user, headers);
//		// Send request with PUT method.
//		restTemplate.postForEntity(URL_USER + "/" + user.getId(), requestBody, UserDTO.class);
//
//	}

}