package com.sp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.DTO.Coord;
import com.DTO.FacilityDTO;
import com.DTO.FireDTO;
import com.DTO.VehicleDTO;
import com.ENUM.VehicleType;
import com.sp.tools.Comm;

/**
 * 
 * @author alexandre.burlot TODO: - faire la téléortation à la fin - pourquoi on
 *         retourne à la facility - comprendre algo au plus proche - séparer en
 *         sous-fonctions - faire des sorties plus propres -
 */

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

			// Regarde le couple {véhiculeId:fireId}
			for (Entry<Integer, Integer> entry : vehicleFireIdMap.entrySet()) {

				vehicleDTO = Comm.getVehicle(entry.getKey()); // Sélection du vehicleDTO
				Coord target; // Définition target du vehicleDTO

				/**
				 * Nouvel algo: - si le véhicule possède un itinéraire: on ne fait rien - si
				 * aucun itinéraire - si target == null - retour à la facility - sinon - target
				 * sur feu
				 */

				if (itinérairesMap.get(vehicleDTO.getId()) == null) {
					if (entry.getValue() == null) {
						System.out.println("\nVehicleDTO id=" + entry.getKey() + " : Le véhicule rentre à la base");
						target = this.targetingFacility();
					} else {
						System.out.println("\nVehicleDTO id=" + entry.getKey() + " Fire id=" + entry.getValue());
						target = this.targetingFire(entry);
					}
					// Création de la route pour aller à la target
					itinérairesMap = this.setRoute(vehicleDTO, target, itinérairesMap);
				} else {
					// Si FireId est null --> Itinéraire vers la base, on le change
					if (entry.getValue() == null) {
						itinérairesMap.put(entry.getKey(), null);
						System.out.println("\nVehicleDTO id=" + entry.getKey() + " Fire id=" + entry.getValue()
								+ " : Réinitialisation itinéraire");
					}
					// TODO: regarder si la dernière coordonnées c'est la base et que FireId != null
					// --> itinéraire à null
//					if (itinérairesMap.get(entry.getKey()).size() > 0) {
//
//						if ((itinérairesMap.get(entry.getKey()).get(itinérairesMap.get(entry.getKey()).size() - 1))
//								.equals(FACILITY_COORDS)) {
//							System.out.println("\nLa destination de Vehicle id=" + entry.getKey()
//									+ " est la caserne alors qu'il devrait aller vers Fire id=" + entry.getValue());
//							itinérairesMap.put(entry.getKey(), null);
//						} 
						else {
							System.out.println("\nVehicleDTO id=" + entry.getKey() + " Fire id=" + entry.getValue()
									+ " : Connaissance de l'itinéraire");
							System.out.println("id=" + vehicleDTO.getId() + " size="
									+ itinérairesMap.get(vehicleDTO.getId()).size() + " "
									+ itinérairesMap.get(vehicleDTO.getId()));
						}
//					}
				}
			}

			// On met à la prochaine coordonnées chaque vehicule
			for (Entry<Integer, List<Coord>> entry : itinérairesMap.entrySet()) {

				vehicleDTO = Comm.getVehicle(entry.getKey());
				List<Coord> routeCoords = entry.getValue();
				if (routeCoords != null) {
					// le véhicule bouge seulement s'il lui reste des déplacements à réaliser
					if (routeCoords.size() > 0) {

						// Possible itinéraire dont la première coordonnées est celle du véhicule
						if (routeCoords.get(0) == new Coord(vehicleDTO.getLon(), vehicleDTO.getLat())) {
							routeCoords.remove(0);
						}

						System.out.println();
						vehicleDTO.setLat(routeCoords.get(0).getLat());
						vehicleDTO.setLon(routeCoords.get(0).getLon());
						routeCoords.remove(0);
						Comm.putUpdateVehicle(vehicleDTO);
					}

					// Si la liste des feux fraîchement récupérée ne contient pas le feu target du
					// véhicule, réinitialise son itinéraire pour renvoie à la facility
					else if (!Comm.getFires().contains(vehicleFireIdMap.get(vehicleDTO.getId()))) {
						itinérairesMap.put(entry.getKey(), null);
					}

					// vient d'arriver sur le feu
					else {
						VehicleType vehicleType = vehicleDTO.getType();
						vehicleDTO.setLiquidQuantity(vehicleDTO.getLiquidQuantity()-2*vehicleType.getLiquidConsumption());
						Comm.putUpdateVehicle(vehicleDTO);
					}
				}
			}
			// Sauvegarde de l'attribution des véhicules sur les feux
			oldMap = vehicleFireIdMap;

			// Attente pour redéclenchement du Thread
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}

	private Coord targetingFacility() {
		Coord target = new Coord(FACILITY_COORDS.getLon(), FACILITY_COORDS.getLat());
		return target;
	}

	private Coord targetingFire(Entry<Integer, Integer> entry) {
		Coord target;
		try {
			FireDTO fireTarget = Comm.getFire(entry.getValue());
			target = new Coord(fireTarget.getLon(), fireTarget.getLat());
		} catch (Exception e) {
			FireDTO fireTarget = Comm.getFire(entry.getValue());
			target = this.targetingFacility();
		}
		return target;
	}

	private Map<Integer, List<Coord>> setRoute(VehicleDTO vehicleDTO, Coord target,
			Map<Integer, List<Coord>> itinérairesMap) {
		List<Coord> newRoute = Comm.getItineraire(new Coord(vehicleDTO.getLon(), vehicleDTO.getLat()),
				new Coord(target.getLon(), target.getLat()));
		newRoute.add(target);
		System.out.println("id=" + vehicleDTO.getId() + " size=" + newRoute.size() + " " + newRoute.toString());
		itinérairesMap.put(vehicleDTO.getId(), newRoute);
		return itinérairesMap;
	}
}