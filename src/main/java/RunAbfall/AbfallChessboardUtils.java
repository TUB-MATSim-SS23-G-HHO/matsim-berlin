package RunAbfall;

import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierCapabilities;
import org.matsim.contrib.freight.carrier.CarrierVehicle;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeLoader;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.carrier.CarrierImpl;

public class AbfallChessboardUtils {

	static String linkChessboardDump = "j(0,9)R";
	static String linkChessboardDepot = "j(9,9)";
	static Carrier carrierChessboard = CarrierImpl.newInstance(Id.create("Carrier_Chessboard", Carrier.class));

	/**
	 * Creates shipments for the chessboard network with the input of the volume
	 * [kg] garbageToCollect.
	 * 
	 * @param
	 */
	static void createShipmentsForChessboardI(HashMap<String, Carrier> carrierMap, int garbageToCollect, Map<Id<Link>, ? extends Link> allLinks,
			double volumeBigDustbin, double serviceTimePerBigTrashcan,
			Scenario scenario, Carriers carriers) {
		Map<Id<Link>, Link> garbageLinks = new HashMap<Id<Link>, Link>();
		carrierMap.clear();
		carrierMap.put("carrierChessboard", carrierChessboard);
		double distanceWithShipments = 0;
		for (Link link : allLinks.values()) {
			if (link.getFreespeed() < 14) {
				garbageLinks.put(link.getId(), link);
				distanceWithShipments = distanceWithShipments + link.getLength();
			}
		}
		Id<Link> linkDumpId = Id.createLinkId(linkChessboardDump);
		AbfallUtils.createShipmentsForCarrierII(garbageToCollect, volumeBigDustbin, serviceTimePerBigTrashcan,
				distanceWithShipments, garbageLinks, scenario, carrierChessboard, linkDumpId, carriers);
		AbfallUtils.districtsWithShipments.add("Chessboard");
		carriers.addCarrier(carrierChessboard);

	}

	/**
	 * Creates shipments for the chessboard network with the input of the volume
	 * [kg] garbagePerMeterToCollect. So every meter of the network gets this volume
	 * of the garbage.
	 * 
	 * @param
	 */
	static void createShipmentsForChessboardII(HashMap<String, Carrier> carrierMap, double garbagePerMeterToCollect, Map<Id<Link>, ? extends Link> allLinks,
			double volumeBigDustbin, double serviceTimePerBigTrashcan,
			Scenario scenario, Carriers carriers) {
		Map<Id<Link>, Link> garbageLinks = new HashMap<Id<Link>, Link>();
		carrierMap.clear();
		carrierMap.put("carrierChessboard", carrierChessboard);
		double distanceWithShipments = 0;
		for (Link link : allLinks.values()) {
			if (link.getFreespeed() < 12) {
				garbageLinks.put(link.getId(), link);
				distanceWithShipments = distanceWithShipments + link.getLength();
			}
		}
		Id<Link> linkDumpId = Id.createLinkId(linkChessboardDump);
		AbfallUtils.createShipmentsForCarrierI(garbagePerMeterToCollect, volumeBigDustbin,
				serviceTimePerBigTrashcan, garbageLinks, scenario, carrierChessboard, linkDumpId, carriers);
		AbfallUtils.districtsWithShipments.add("Chessboard");
		carriers.addCarrier(carrierChessboard);

	}

	/**
	 * Creates the vehicle at the depot, ads this vehicle to the carriers and sets
	 * the capabilities. This method is for the Chessboard network with one depot.
	 * 
	 * @param
	 */
	static void createCarriersForChessboard(Carriers carriers, FleetSize fleetSize) {
		String vehicleName = "TruckChessboard";
		double earliestStartingTime = 6 * 3600;
		double latestFinishingTime = 15 * 3600;

		AbfallUtils.createGarbageTruck(vehicleName, linkChessboardDepot, earliestStartingTime, latestFinishingTime);

		// define Carriers

		defineCarriersChessboard(carriers, AbfallUtils.vehicleAtDepot, fleetSize);
	}

	/**
	 * Defines and sets the Capabilities of the Carrier, including the vehicleTypes
	 * for the carriers for the Chessboard network
	 * 
	 * @param
	 * 
	 */
	private static void defineCarriersChessboard(Carriers carriers, CarrierVehicle vehicleDepot, FleetSize fleetSize) {
		CarrierCapabilities carrierCapabilities = CarrierCapabilities.Builder.newInstance()
				.addType(AbfallUtils.carrierVehType).addVehicle(vehicleDepot).setFleetSize(fleetSize).build();

		carrierChessboard.setCarrierCapabilities(carrierCapabilities);

		// Fahrzeugtypen den Anbietern zuordenen
		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(AbfallUtils.vehicleTypes);
	}
}
