package org.matsim.analysis;

import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;


public class FindVehicleStayPerLink {

    public static void main(String[] args) {

        var network = NetworkUtils.readNetwork("./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_base/berlin-v5.5-1pct.output_network.xml.gz");
//        var network = NetworkUtils.readNetwork("./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_HalfCircleOutput/berlin-v5.5-1pct.output_network.xml.gz");
//        var network = NetworkUtils.readNetwork("./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_FullCircleOutput/berlin-v5.5-1pct.output_network.xml.gz");

        var vehicleLinkEnterLeaveTimeHandler = new VehicleLinkEnterLeaveTimeHandler(network);

        var manager = EventsUtils.createEventsManager();
        manager.addHandler(vehicleLinkEnterLeaveTimeHandler);

        EventsUtils.readEvents(manager, "./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_base/berlin-v5.5-1pct.output_events.xml.gz");
//        EventsUtils.readEvents(manager, "./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_HalfCircleOutput/berlin-v5.5-1pct.output_events.xml.gz");
//        EventsUtils.readEvents(manager, "./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_FullCircleOutput/berlin-v5.5-1pct.output_events.xml.gz");

        var vehicleLinkEnterLeaveTime = vehicleLinkEnterLeaveTimeHandler.getVehicleLinkEnterLeaveTime();

        vehicleLinkEnterLeaveTime.forEach((link, vehicles) -> {
            vehicles.forEach((vehicleId, startEndTimePair) -> {
                Collections.sort(startEndTimePair.getKey());
                Collections.sort(startEndTimePair.getValue());
            });
        });

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("VehicleStayPerLink_base.csv"));
//            BufferedWriter writer = new BufferedWriter(new FileWriter("VehicleStayPerLink_HalfCircle.csv"));
//            BufferedWriter writer = new BufferedWriter(new FileWriter("VehicleStayPerLink_FullCircle.csv"));

            // Write the header line to the CSV file
            writer.write("link_id,vehicle_id,link_enter_e_time,link_leave_e_time,stay_time");
            writer.newLine();

            vehicleLinkEnterLeaveTime.forEach((link, vehicles) -> {
                vehicles.forEach((vehicleId, startEndTimePair) -> {
                    var enterEventTimes = startEndTimePair.getKey();
                    var leaveEventTimes = startEndTimePair.getValue();

                    for (int i = 0; i < leaveEventTimes.size(); i++) {
                        if (i < enterEventTimes.size()) {
                            var stay_time = leaveEventTimes.get(i) - enterEventTimes.get(i);

                            try {
//                                writer.write(link.getId() + "," + vehicleId + "," + stay_time);
                                writer.write(link.getId() + "," + vehicleId + "," + enterEventTimes.get(i) + "," + leaveEventTimes.get(i) + "," + stay_time);
                                writer.newLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            });

            System.out.println("Write to file completed successfully.");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
