package org.matsim.analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.api.core.v01.network.Link;


public class FindKmTravelledPerLink {

    public static void main(String[] args) {

        var network = NetworkUtils.readNetwork("./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_base/berlin-v5.5-1pct.output_network.xml.gz");
//        var network = NetworkUtils.readNetwork("./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_HalfCircleOutput/berlin-v5.5-1pct.output_network.xml.gz");
//        var network = NetworkUtils.readNetwork("./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_FullCircleOutput/berlin-v5.5-1pct.output_network.xml.gz");

        var linkEnterEventCountHandler = new LinkEnterEventCountHandler(network);

        var manager = EventsUtils.createEventsManager();
        manager.addHandler(linkEnterEventCountHandler);

        EventsUtils.readEvents(manager, "./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_base/berlin-v5.5-1pct.output_events.xml.gz");
//        EventsUtils.readEvents(manager, "./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_HalfCircleOutput/berlin-v5.5-1pct.output_events.xml.gz");
//        EventsUtils.readEvents(manager, "./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_FullCircleOutput/berlin-v5.5-1pct.output_events.xml.gz");

        var linkEnterEventsCount = linkEnterEventCountHandler.getLinkEnterEventsCount();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("KmTravelledPerLink_base.csv"));
//            BufferedWriter writer = new BufferedWriter(new FileWriter("KmTravelledPerLink_HalfCircle.csv"));
//            BufferedWriter writer = new BufferedWriter(new FileWriter("KmTravelledPerLink_FullCircle.csv"));

            // Write the header line to the CSV file
            writer.write("link_id,km_travelled");
            writer.newLine();

            for (Entry<Link, Integer> entry : linkEnterEventsCount.entrySet()) {
                Link link = entry.getKey();
                int count = entry.getValue();

                writer.write(link.getId() + "," + link.getLength() * count / 1000);
                writer.newLine();
            }

            System.out.println("Write to file completed successfully.");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
