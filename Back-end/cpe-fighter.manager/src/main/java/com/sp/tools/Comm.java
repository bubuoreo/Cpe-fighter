package com.sp.tools;

import org.springframework.web.client.RestTemplate;

import com.DTO.Coord;
import com.DTO.FacilityDTO;
import com.DTO.FireDTO;
import com.DTO.VehicleDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Comm {

	// "http://localhost:8081/fire";
	private static final String URL_FIRE = "http://localhost:8081/fire";
	private static final String URL_FACILITY = "http://vps.cpe-sn.fr:8081/facility";
	private static final String URL_API_VEHICLE = "http://vps.cpe-sn.fr:8081/vehicle";
//	private static final String URL_API_VEHICLE = "http://localhost:8082/vehicle";
	private static final String TEAM_UUID = "c230e0e0-8de9-4c39-8dec-e246dc0c6334";
	private static final Integer FACILITY_ID = 267;
	private static final String URL_MAPBOX_API = "https://api.mapbox.com/directions/v5/mapbox/driving";
	private static final String URL_MAPBOX_API_ARG = "?access_token=pk.eyJ1IjoicXVlbnRpbm1lbGVybyIsImEiOiJjbDQzdHlib3UwODBlM2ptbm5pbTB0a241In0.D84kA0rZXZoAHacLB6DY4w&geometries=geojson";
	private static RestTemplate restTemplate = new RestTemplate();

	public static List<FireDTO> getFires() {

//		System.out.println("requête pour obtenir les feux");
		// Send request with GET method and default Headers.
		FireDTO[] fireDTOs = restTemplate.getForObject(URL_FIRE, FireDTO[].class);
		List<FireDTO> firesList = new ArrayList<FireDTO>();
		for (FireDTO fireDTO : fireDTOs) {
			firesList.add(fireDTO);
		}
		return firesList;
	}

	public static FireDTO getFire(Integer id) {
//		System.out.println("requête pour obtenir les feux");
		// Send request with GET method and default Headers.
		FireDTO fire = restTemplate.getForObject(URL_FIRE + "/" + id, FireDTO.class);
		return fire;
	}

	public static List<FacilityDTO> getFacility() {

//		System.out.println("requête pour obtenir toutes les facilities");
		// Send request with GET method and default Headers.
		FacilityDTO[] facilityDTOs = restTemplate.getForObject(URL_FACILITY, FacilityDTO[].class);
		List<FacilityDTO> facilitiesList = new ArrayList<FacilityDTO>();
		for (FacilityDTO facilityDTO : facilityDTOs) {
			facilitiesList.add(facilityDTO);
		}
		return facilitiesList;
	}
	
	public static FacilityDTO getFacility(Integer facilityId) {

//		System.out.println("requête pour obtenir notre facility");
		// Send request with GET method and default Headers.
		FacilityDTO facility = restTemplate.getForObject(URL_FACILITY + "/" + facilityId, FacilityDTO.class);
		return facility;
	}

	public static List<VehicleDTO> getVehicles() {

//		System.out.println("requête pour obtenir les vehicules");
		// Send request with GET method and default Headers.
		VehicleDTO[] vehicleDTOs = restTemplate.getForObject(URL_API_VEHICLE, VehicleDTO[].class);
		List<VehicleDTO> vehiclesList = new ArrayList<VehicleDTO>();
		for (VehicleDTO vehicleDTO : vehicleDTOs) {
			vehiclesList.add(vehicleDTO);
		}
		return vehiclesList;
	}

	public static void putUpdateVehicle(VehicleDTO vehicleDTO) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<VehicleDTO> requestBody = new HttpEntity<>(vehicleDTO, headers);
		// Send request with PUT method.
		restTemplate.put(URL_API_VEHICLE + "/" + TEAM_UUID + "/" + vehicleDTO.getId(), requestBody, VehicleDTO.class);
	}

	public static VehicleDTO getVehicle(Integer id) {
//		System.out.println("requête pour obtenir le vehicule avec l'id=" + id);
		// Send request with GET method and default Headers.
		VehicleDTO vehicleDTOs = restTemplate.getForObject(URL_API_VEHICLE + "/" + id, VehicleDTO.class);
		return vehicleDTOs;
	}

	public static List<Coord> getItineraire(Coord depart, Coord arrive) {
		String itineraireJson = restTemplate.getForObject(
				URL_MAPBOX_API + "/" + depart.getLon() + "," + depart.getLat() + ";" + arrive.getLon() + "," + arrive.getLat() + URL_MAPBOX_API_ARG,
				String.class);
		System.out.println();
		// Extraction des coordonnées gps de la reponse
		JSONObject jsonReponse = new JSONObject(itineraireJson);

		JSONArray arrayRoute = jsonReponse.getJSONArray("routes");
		JSONObject routeJson = new JSONObject(arrayRoute.get(0).toString());

		JSONObject geometrie = routeJson.getJSONObject("geometry");

		JSONArray coordinates = geometrie.getJSONArray("coordinates");

		// conversion des coodonées GPS et conversion en obj Coords
		List<Coord> trajetCoord = new ArrayList<Coord>();
		for (int i = 0; i < coordinates.length(); i++) {
			String[] coords = coordinates.get(i).toString().split(",");

			StringBuffer coord1String = new StringBuffer(coords[0]);
			StringBuffer coord2String = new StringBuffer(coords[1]);

			double lon = Double.parseDouble(coord1String.deleteCharAt(0).toString());
			double lat = Double.parseDouble(coord2String.deleteCharAt(coord2String.length() - 1).toString());

			trajetCoord.add(new Coord(lon, lat));

		}
		return trajetCoord;

	}

}