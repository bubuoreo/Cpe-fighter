package com.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.DTO.Coord;
import com.DTO.FacilityDTO;
import com.DTO.FireDTO;
import com.DTO.VehicleDTO;
import com.sp.tools.Comm;

@Service
public class ManagerService {
	
	private static final Integer FACILITY_ID = 267;
	private static final FacilityDTO FACILITY_DTO = Comm.getFacility(FACILITY_ID);
	private static final Coord FACILITY_COORDS = new Coord(FACILITY_DTO.getLon(), FACILITY_DTO.getLat());

	InterventionRunnable mRunnable;
	private Thread modelThread;
	
	
	public ManagerService() {
		this.mRunnable=new InterventionRunnable();
		modelThread=new Thread(mRunnable);
	}
	
	public void lancement() {
		modelThread.start();
		mRunnable.lancementMouvement();
	}

	public boolean returnTofacility(VehicleDTO vehicleDTO) {
		boolean ret = false;
		vehicleDTO.setLat(FACILITY_COORDS.getLon());
		vehicleDTO.setLon(FACILITY_COORDS.getLat());
		return ret;
	}
	
	public List<VehicleDTO> getVehicles() {
		List<VehicleDTO> vehiclesList = Comm.getVehicles();
		return vehiclesList;
	}

	public List<FireDTO> getFires() {
		List<FireDTO> firesList = Comm.getFires();
		return firesList;
	}
	
}
