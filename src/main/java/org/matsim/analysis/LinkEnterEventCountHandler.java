package org.matsim.analysis;

import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.*;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;

import java.util.*;

public class LinkEnterEventCountHandler implements LinkEnterEventHandler {

    private final Map<Link, Integer> linkEnterEventsCount = new HashMap<>();

    private final Network network;

    public LinkEnterEventCountHandler(Network network) {
        this.network = network;
    }

    public Map<Link, Integer> getLinkEnterEventsCount() {
        return linkEnterEventsCount;
    }

    @Override
    public void handleEvent(LinkEnterEvent linkEnterEvent) {

        var link = network.getLinks().get(linkEnterEvent.getLinkId());

        var currentValue = linkEnterEventsCount.getOrDefault(link, 0);

        linkEnterEventsCount.put(link, currentValue + 1);
    }
}
