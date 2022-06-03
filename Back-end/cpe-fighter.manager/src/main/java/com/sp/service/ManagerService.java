package com.sp.service;

import java.util.ArrayList;
import java.util.List;

import com.DTO.FireDTO;
import com.DTO.VehicleDTO;
import com.sp.tools.Comm;

public class ManagerService {
	
	private static final Integer facilityRefId = 267;
	// contient les coordonnée de la caserne sous la forme [lattitude, longitude]
	// à changer par un objet de type de Coord donné par le prof
	private static final Double[] facilityCoords = {(Double) 45.779367096682726,(Double) 4.859884072903303};

	public List<Object> manageFire() {
		List<FireDTO> firesList = Comm.getFires();
		FireDTO fireTarget = firesList.get(0);
		List<VehicleDTO> vehiclesList = Comm.getVehicles();
		List<Object> returnList = new ArrayList<Object>();
		returnList.add(firesList.get(0));
		List<VehicleDTO> tmp = new ArrayList<VehicleDTO>();
		for (VehicleDTO vehicleDTO : vehiclesList) {
			if ((int) vehicleDTO.getFacilityRefID() == (int) facilityRefId) {
				System.out.println(vehicleDTO.getId());
				tmp.add(vehicleDTO);
			}
		}
		
		for (VehicleDTO vehicleDTO : tmp) {
			if (vehicleDTO.getLat() == facilityCoords[0] && vehicleDTO.getLon() == facilityCoords[1]) {
				returnList.add(vehicleDTO);
				vehicleDTO.setLat(fireTarget.getLat());
				vehicleDTO.setLon(fireTarget.getLon());
				Comm.putUpdateVehicle(vehicleDTO);
			}
		}
		return returnList;
	}
}
