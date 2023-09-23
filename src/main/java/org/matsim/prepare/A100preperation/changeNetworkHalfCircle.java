package org.matsim.prepare.A100preperation;

import org.matsim.api.core.v01.Id;
import org.matsim.core.network.NetworkUtils;

import java.util.*;

// apply network changes to extend the A100 highway to Storkower Straße
public class changeNetworkHalfCircle {

    public static void main(String[] args) {
        var network = NetworkUtils.readNetwork("./scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network.xml.gz");

        List<String[]> newLinkAttributes = new ArrayList<>();

        // linkID, fromNode, toNode, linkLength
        newLinkAttributes.add(new String[]{"a100_01n", "206191207","5332081036","900.0"});
        newLinkAttributes.add(new String[]{"a100_01s", "3386901047","27542427", "900.0"});
        newLinkAttributes.add(new String[]{"a100_02n", "5332081036","287932598", "2300.0"});
        newLinkAttributes.add(new String[]{"a100_02s", "260742575","3386901047", "2300.0"});
        newLinkAttributes.add(new String[]{"a100_03n", "287932598","2395404884", "1500.0"});
        newLinkAttributes.add(new String[]{"a100_03s", "2395404884","260742575", "1500.0"});
        newLinkAttributes.add(new String[]{"a100_04n", "2395404884","52762606", "1450.0"});
        newLinkAttributes.add(new String[]{"a100_04s", "52762606","2395404884", "1450.0"});
        newLinkAttributes.add(new String[]{"a100_05n", "52762606","28373619", "1100.0"});
        newLinkAttributes.add(new String[]{"a100_05s", "28373623","52762606", "1100.0"});



        // create links
        for (String[] entry : newLinkAttributes) {
            String linkID = entry[0];
            var fromNode = network.getNodes().get(Id.createNodeId(entry[1]));
            var toNode = network.getNodes().get(Id.createNodeId(entry[2]));
            double linkLength = Double.parseDouble(entry[3]);
            double freeSpeed = 16.667;
            double capacity = 4000.0;
            double permLanes = 2.0;
            Set<String> modes = new HashSet<>();
            modes.add("car");
            modes.add("freight");
            modes.add("ride");

            var newLink = network.getFactory().createLink(Id.createLinkId(linkID), fromNode, toNode);

            // set properties
            newLink.setFreespeed(freeSpeed);
            newLink.setLength(linkLength);
            newLink.setCapacity(capacity);
            newLink.setNumberOfLanes(permLanes);
            newLink.setAllowedModes(modes);

            network.addLink(newLink);
        }

        NetworkUtils.writeNetwork(network, "./scenarios/berlin-v5.5-1pct/input/berlin-v5.5-network_A100_halfCircle.xml.gz");


    }
}
