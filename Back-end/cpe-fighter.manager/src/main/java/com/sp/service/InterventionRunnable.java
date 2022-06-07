package com.sp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	
	private static final Integer facilityRefId = 267;
	private static final Double[] FACILITY_COORDS = {(Double) 45.779367096682726,(Double) 4.859884072903303};

	boolean isEnd;
	private List<FireDTO> fireList;

	public InterventionRunnable() {
		super();
		this.isEnd = false;
		this.fireList = new ArrayList<FireDTO>();
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
		Map<Integer, Integer> vehicleFireIdMap;
		vehicleFireIdMap = new HashMap<Integer, Integer>();
		
		System.out.println("création de la HashMap");
		// création de la HashMap
		List<VehicleDTO> vehiclesList = Comm.getVehicles();
		for (VehicleDTO vehicleDTO : vehiclesList) {
			if ((int) vehicleDTO.getFacilityRefID() == (int) facilityRefId) {
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
			System.out.println("Obtention de la nouvelle liste de feux");
			// parcours double pour identifier les nouveaux feux et les feux venant d'être éteint
			// on propose un changement en hashmap pour de l'optimisation
			System.out.println("Création liste des nouveaux feux");
			for (FireDTO fireDTO : tmp) {
				// Si un de la liste actualisée des feux n'est pas dans l'ancienne
				if (!fireList.contains(fireDTO)) {
					newFireIdList.add(fireDTO.getId());
				}
			}
			System.out.println("Création liste des feux éteint depuis la dernière fois");
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
			System.out.println("gestion rapatriements");
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
			System.out.println("Gestion des nouveaux feux");
			for(Entry<Integer, Integer> entry: vehicleFireIdMap.entrySet()) {
				// envoyer le véhicule sur le feu car non affecté à un feu
				if (entry.getValue() == null) {
					System.out.println("Il y a des véhicules sans activité");
					// si il reste encore des feux à attribuer
					if (newFireIdList.size() != 0) {
						System.out.println("Il y a des nouveaux feux à éteindre");
						VehicleDTO vehicleDTO = Comm.getVehicle(entry.getKey());
						// déclaration de la target feu
						FireDTO fireTarget = Comm.getFire(newFireIdList.get(0));
						newFireIdList.remove(0);
						// placement du véhicule sur le feu cible
						System.out.println("Attribution au véhicule id="+vehicleDTO.getId()+" a feu id="+fireTarget.getId());
						vehicleDTO.setLat(fireTarget.getLat());
						vehicleDTO.setLon(fireTarget.getLon());
						// PUT pouyr téléportation vehicule
						Comm.putUpdateVehicle(vehicleDTO);
						// MAJ de la map pour dire que le véhicule s'occupe du fireTarget
						vehicleFireIdMap.put(vehicleDTO.getId(), fireTarget.getId());
					}
					else {
						
						callbackVehicleList.add(entry.getKey());
					}
				}
		    }
			
			System.out.println("Les véhicules rentrent à la base");
			// les vehicules dans callbackVehicleList doivent rentrer.
			for (Integer id : callbackVehicleList) {
				VehicleDTO vehicleDTO = Comm.getVehicle(id);
				vehicleDTO.setLat(FACILITY_COORDS[0]);
				vehicleDTO.setLon(FACILITY_COORDS[1]);
				Comm.putUpdateVehicle(vehicleDTO);
			}
			
			// Sauvegarder la liste tmp en temps que fireList
			fireList = tmp;
			
			System.out.println("On attend");
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Runnable DisplayRunnable ends.... ");
	}

	public void stop() {
		this.isEnd = true;
	}

}
