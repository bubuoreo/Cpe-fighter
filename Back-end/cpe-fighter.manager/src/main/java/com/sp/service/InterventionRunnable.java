package com.sp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.DTO.Coord;
import com.DTO.FireDTO;
import com.DTO.VehicleDTO;
import com.sp.tools.Comm;

/*
 * TODO :
 * - séparation des fonctions de la méthode run sur plusieurs 
 * 		petites méthodes pour plus de lisibilité
 * - revoir la façon dont les vehicules sont réatribués à des 
 * 		nouveaux feux quand l'escence sera prise en compte
 * 
 */

public class InterventionRunnable implements Runnable {
	
	private static final Integer FACILITY_ID = 267;
	private static final Coord FACILITY_COORDS = new Coord(45.779367096682726,4.859884072903303);

	boolean isEnd;
	private List<FireDTO> fireList;
	private Map<Integer, Integer> vehicleFireIdMap;
	boolean newAcquire;
	
	MouvementRunnable moveRunnable;
	private Thread moveThread;
	

	public InterventionRunnable() {
		super();
		this.isEnd = false;
		this.newAcquire = false;
		this.fireList = new ArrayList<FireDTO>();
		this.vehicleFireIdMap = new HashMap<Integer, Integer>();
		this.moveRunnable=new MouvementRunnable(vehicleFireIdMap);
		moveThread=new Thread(moveRunnable);
		
	}
	
	public void lancementMouvement() {
		moveThread.start();
		
	}
	
	/*
	 * Logique de la fonction:
	 * si la liste est vide au début ?
	 * Si elle est pas vide :
	 * 	- on regarde si tous les feux de la nouvelle liste sont dans la fireList
	 * 	- si oui --> on ne fait rien et le Tread se rendors
	 * 	- si un feu de tmp n'est pas dans la liste fireList, c'est un nouveau feu donc:
	 * 		- assignation d'un véhicule si disponible (en caserne (ou bien si on y arrive) quand il est encore en vadrouille)
	 * 	- si un feu de la fireList n'est pas dans tmp, feu qui vient d'être éteint:
	 * 		- rappel de l'équipe en charge si c'était nous 
	 */
	@Override
	public void run() {
		
		List<Integer> newFireIdList;
		List<Integer> callbackVehicleList;
		List<Integer> disapearedFireIdList;
		
		System.out.println("InterventionRunnable: création de la HashMap");
		// création de la HashMap
		List<VehicleDTO> vehiclesList = Comm.getVehicles();
		for (VehicleDTO vehicleDTO : vehiclesList) {
			if ((int) vehicleDTO.getFacilityRefID() == (int) FACILITY_ID) {
				System.out.println(vehicleDTO.getId());
				vehicleFireIdMap.put(vehicleDTO.getId(), null);
			}
		}
				
		while (!this.isEnd) {
			newFireIdList = new ArrayList<Integer>();
			disapearedFireIdList = new ArrayList<Integer>();
			callbackVehicleList = new ArrayList<Integer>();
			// contient les couples {vehicleId,fireId}
			List<FireDTO> tmp = Comm.getFires();
			// parcours double pour identifier les nouveaux feux et les feux venant d'être éteint
			// on propose un changement en hashmap pour de l'optimisation
			for (FireDTO fireDTO : tmp) {
				// Si un de la liste actualisée des feux n'est pas dans l'ancienne
				if (!fireList.contains(fireDTO)) {
					newFireIdList.add(fireDTO.getId());
				}
			}
			for (FireDTO fireDTO : fireList) {
				// Si un de l'ancienne liste des feux n'est pas dans la nouvelle
				if (!tmp.contains(fireDTO)) {
					disapearedFireIdList.add(fireDTO.getId());
				}
			}
			/*
			 * On a les nouveaux feux et les feux qui viennent de s'éteindre
			 * gestion rapatriement des camions en charge des feux disparus
			 */
			for(Entry<Integer, Integer> entry: vehicleFireIdMap.entrySet()) {
				if (disapearedFireIdList.contains(entry.getValue())) {
					// MAJ map --> passe l'attribution de son feu à null
					entry.setValue(null);
				}
		    }
			/*
			 * On possède une liste de véhicules qu'on peut rentrer à la caserne
			 * On passe à la gestion des nouveaux feux
			 */
			for(Entry<Integer, Integer> entry: vehicleFireIdMap.entrySet()) {
				// envoyer le véhicule sur le feu car non affecté à un feu
				if (entry.getValue() == null) {
					System.out.println("InterventionRunnable: Le véhicules id="+entry.getKey()+" est sans activité");
					// si il reste encore des feux à attribuer
					if (newFireIdList.size() != 0) {
						VehicleDTO vehicleDTO = Comm.getVehicle(entry.getKey());
						// déclaration de la target feu
						FireDTO fireTarget = this.closestFire(vehicleDTO, newFireIdList);
						newFireIdList.remove(newFireIdList.indexOf(fireTarget.getId()));
						// placement du véhicule sur le feu cible
						System.out.println("InterventionRunnable: Attribution au véhicule id="+vehicleDTO.getId()+" a feu id="+fireTarget.getId());
//						vehicleDTO.setLat(fireTarget.getLat());
//						vehicleDTO.setLon(fireTarget.getLon());
//						// PUT pouyr téléportation vehicule
//						Comm.putUpdateVehicle(vehicleDTO);
						// MAJ de la map pour dire que le véhicule s'occupe du fireTarget
						vehicleFireIdMap.put(vehicleDTO.getId(), fireTarget.getId());
					}
					else {
						
						callbackVehicleList.add(entry.getKey());
					}
				}
		    }
			
			// Sauvegarder la liste tmp en temps que fireList
			fireList = tmp;
			this.newAcquire=true;
			
			System.out.println("InterventionRunnable: On attend");
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Runnable DisplayRunnable ends.... ");
	}

	public void stop() {
		this.isEnd = true;
	}
	
	private FireDTO closestFire(VehicleDTO vehicleDTO,List<Integer> newFireIdList) {
		FireDTO fireTarget = null;
		FireDTO fireDTO;
		for (Integer fireDTOId : newFireIdList) {
			if (fireTarget == null) {
				fireTarget = Comm.getFire(fireDTOId);
			}
			else {
				try {
					fireDTO = Comm.getFire(fireDTOId);
					if (Math.pow(fireDTO.getLon()-vehicleDTO.getLon(),2) + Math.pow(fireDTO.getLat()-vehicleDTO.getLat(),2) <
							Math.pow(fireTarget.getLon()-vehicleDTO.getLon(),2) + Math.pow(fireTarget.getLat()-vehicleDTO.getLat(),2)) {
						fireTarget = fireDTO;
					}
				} catch (Exception e) {
					System.out.println("Le feu est null, suivant !!");
				}
			}
		}
		return fireTarget;
	}
}
