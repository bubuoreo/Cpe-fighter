package com.sp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.DTO.Coord;
import com.DTO.FacilityDTO;
import com.DTO.FireDTO;
import com.DTO.VehicleDTO;
import com.sp.tools.Comm;

public class MouvementRunnable implements Runnable {

	private Map<Integer, Integer> vehicleFireIdMap;
	private Boolean isEnd;
	private static final Integer FACILITY_ID = 267;
	private static final FacilityDTO FACILITY_DTO = Comm.getFacility(FACILITY_ID);
	private static final Coord FACILITY_COORDS = new Coord(FACILITY_DTO.getLon(), FACILITY_DTO.getLat());

	public MouvementRunnable(Map<Integer, Integer> vehicleFireIdMap) {
		super();
		this.isEnd = false;
		this.vehicleFireIdMap = vehicleFireIdMap;
	}

	/**
	 * Logique métier - récupération des valeurs de la map - regarder la trajectoire
	 * pour les feux attribués TODO: faire en sorte que l'itinéraire ne soit pas
	 * calculé toutes les 2 secondes
	 */
	public void run() {
		System.out.println("Lancement du MouvementRunnable");
		Map<Integer, Integer> oldMap = vehicleFireIdMap;
		Map<Integer, List<Coord>> itinérairesMap = new HashMap<Integer, List<Coord>>();
		VehicleDTO vehicleDTO;
		List<Coord> newRoute;
		while (!this.isEnd) {
			// parcours les attributions des feux
			for (Entry<Integer, Integer> entry : vehicleFireIdMap.entrySet()) {
				Coord target;
				vehicleDTO = Comm.getVehicle(entry.getKey());
				// regarde si l'attribution a changé
				if (entry.getValue() != oldMap.get(entry.getKey())) {
					System.out.println("MouvementRunnable: L'affectation du véhicule id=" + entry.getKey() + " a changé pour le feu id="
							+ entry.getValue());
					System.out.println("MouvementRunnable: Calcul de la nouvelle route pour ce véhicule");
					// Si l'attribution à changé poour un null, on rentre à la base
					if (entry.getValue() == null) {
						System.out.println("MouvementRunnable: Le véhicule rentre à la base par un itinnéraire calculé");
						target = new Coord(FACILITY_COORDS.getLon(), FACILITY_COORDS.getLat());
					}
					// Itinéraire vers le nouveau feu
					else {
						System.out.println("MouvementRunnable: On calcul l'itinéraire vers le feu");
						FireDTO fireTarget = Comm.getFire(entry.getValue());
						target = new Coord(fireTarget.getLon(), fireTarget.getLat());
					}
					newRoute = Comm.getItineraire(new Coord(vehicleDTO.getLon(), vehicleDTO.getLat()),
							new Coord(target.getLon(), target.getLat()));
					System.out.println(newRoute);
					itinérairesMap.put(vehicleDTO.getId(), newRoute);
				} else if (itinérairesMap.get(vehicleDTO.getId()) != null) {
					System.out.println("MouvementRunnable: L'affectation du véhicule id=" + entry.getKey()
							+ " n'a pas changé et est toujours le feu id=" + entry.getValue());
				}
				else {
					try {
						// plante quand le feu à été éteint
						FireDTO fireTarget = Comm.getFire(entry.getValue());
						target = new Coord(fireTarget.getLon(), fireTarget.getLat());
						System.out.println("MouvementRunnable: la destination est à Longitude="+target.getLon()+" Lattitude="+target.getLat());
						newRoute = Comm.getItineraire(new Coord(vehicleDTO.getLon(), vehicleDTO.getLat()),
								new Coord(target.getLon(), target.getLat()));
						itinérairesMap.put(vehicleDTO.getId(), newRoute);
					} catch (Exception e) {
						System.out.println("MouvementRunnable Erreur: retour à la base");
						target = new Coord(FACILITY_COORDS.getLon(), FACILITY_COORDS.getLat());
						newRoute = Comm.getItineraire(new Coord(vehicleDTO.getLon(), vehicleDTO.getLat()),
								new Coord(target.getLon(), target.getLat()));
						System.out.println(newRoute);
						itinérairesMap.put(vehicleDTO.getId(), newRoute);
					}
					
				}
				System.out.println(itinérairesMap.get(vehicleDTO.getId()));
			}
			// 
			System.out.println("MouvementRunnable: On attribut à chaque véhicule son prochain emplacement");
			for (Entry<Integer, List<Coord>> entry : itinérairesMap.entrySet()) {
				vehicleDTO = Comm.getVehicle(entry.getKey());
				List<Coord> routeCoords = entry.getValue();
				// le véhicule bouge seulement s'il possède une destination
				if (routeCoords.size() > 1) {
					routeCoords.remove(0);
					System.out.println("MouvementRunnable: Vehicle id="+vehicleDTO.getId()+" vers cible ={"+routeCoords.get(0).getLat()+","+routeCoords.get(0).getLon()+"}");
					vehicleDTO.setLat(routeCoords.get(0).getLat());
					vehicleDTO.setLon(routeCoords.get(0).getLon());
					routeCoords.remove(0);
					Comm.putUpdateVehicle(vehicleDTO);
				}
				else if (!Comm.getFires().contains(vehicleFireIdMap.get(vehicleDTO.getId()))) {
					itinérairesMap.put(entry.getKey(), null);
				}
			}
			// Sauvegarde de l'attribution des véhicules sur les feux
			oldMap = vehicleFireIdMap;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}

	public boolean returnTofacility(VehicleDTO vehicleDTO) {
		boolean ret = false;
		vehicleDTO.setLat(FACILITY_COORDS.getLon());
		vehicleDTO.setLon(FACILITY_COORDS.getLat());
		return ret;
	}
}
