package org.matsim.analysis;

import javafx.util.Pair;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.vehicles.Vehicle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VehicleLinkEnterLeaveTimeHandler implements LinkEnterEventHandler, LinkLeaveEventHandler {

    private final Network network;
    public Map<Link, Map<Id<Vehicle>, Pair<List<Double>, List<Double>>>> vehicleLinkEnterLeaveTime = new HashMap<>();

    public VehicleLinkEnterLeaveTimeHandler(Network network) {
        this.network = network;
    }

    public Map<Link, Map<Id<Vehicle>, Pair<List<Double>, List<Double>>>> getVehicleLinkEnterLeaveTime() {
        return vehicleLinkEnterLeaveTime;
    }

    @Override
    public void handleEvent(LinkEnterEvent linkEnterEvent) {

        var link = network.getLinks().get(linkEnterEvent.getLinkId());
        vehicleLinkEnterLeaveTime.putIfAbsent(link, new HashMap<>());
        // Considering a vehicle can enter a link multiple times keeping enter events time in key array list for the vehicle
        vehicleLinkEnterLeaveTime.get(link).putIfAbsent(linkEnterEvent.getVehicleId(), new Pair<>(new ArrayList<>(), new ArrayList<>()));
        vehicleLinkEnterLeaveTime.get(link).get(linkEnterEvent.getVehicleId()).getKey().add(linkEnterEvent.getTime());
    }

    @Override
    public void handleEvent(LinkLeaveEvent linkLeaveEvent) {
        var link = network.getLinks().get(linkLeaveEvent.getLinkId());
        vehicleLinkEnterLeaveTime.putIfAbsent(link, new HashMap<>());
        // Considering a vehicle can leave a link multiple times keeping leave events time in key array list for the vehicle
        vehicleLinkEnterLeaveTime.get(link).putIfAbsent(linkLeaveEvent.getVehicleId(), new Pair<>(new ArrayList<>(), new ArrayList<>()));
        vehicleLinkEnterLeaveTime.get(link).get(linkLeaveEvent.getVehicleId()).getValue().add(linkLeaveEvent.getTime());
    }
}
