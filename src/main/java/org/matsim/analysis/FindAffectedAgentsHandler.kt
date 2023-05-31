package org.matsim.analysis

import org.matsim.api.core.v01.Id
import org.matsim.api.core.v01.Id.createLinkId
import org.matsim.api.core.v01.events.LinkLeaveEvent
import org.matsim.api.core.v01.events.PersonEntersVehicleEvent
import org.matsim.api.core.v01.events.PersonLeavesVehicleEvent
import org.matsim.api.core.v01.events.TransitDriverStartsEvent
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler
import org.matsim.api.core.v01.events.handler.PersonEntersVehicleEventHandler
import org.matsim.api.core.v01.events.handler.PersonLeavesVehicleEventHandler
import org.matsim.api.core.v01.events.handler.TransitDriverStartsEventHandler
import org.matsim.api.core.v01.network.Link
import org.matsim.api.core.v01.population.Person
import org.matsim.vehicles.Vehicle

class FindAffectedAgentsHandler : TransitDriverStartsEventHandler,
    PersonEntersVehicleEventHandler, LinkLeaveEventHandler, PersonLeavesVehicleEventHandler {

    private val A100_LINKS: Set<Id<Link>> = getLinkIdList().toSet()
    private val personsInCar: MutableMap<Id<Vehicle>, Id<Person>> = HashMap()
    private val transitDrivers: MutableSet<Id<Person>> = HashSet()
    private val agents: MutableSet<Id<Person>> = HashSet()
    private val a100Users: MutableSet<Id<Person>> = HashSet()

    fun getAgents(): Set<Id<Person>> {
        return agents
    }

    fun getA100Users(): Set<Id<Person>> {
        return a100Users
    }

    override fun handleEvent(event: TransitDriverStartsEvent) {
        transitDrivers.add(event.driverId)
    }

    override fun handleEvent(event: PersonEntersVehicleEvent) {
        if (transitDrivers.contains(event.personId)) {
            return
        }
        personsInCar[event.vehicleId] = event.personId
        agents.add(event.personId)
    }

    override fun handleEvent(event: LinkLeaveEvent) {
        if (!A100_LINKS.contains(event.linkId)) {
            return
        }
        // Agents leaving A100
        personsInCar[event.vehicleId]?.let { a100Users.add(it) }
    }

    override fun handleEvent(event: PersonLeavesVehicleEvent) {
        if (transitDrivers.contains(event.personId)) {
            return
        }
        personsInCar.remove(event.vehicleId)
    }

    private fun getLinkIdList(): List<Id<Link>> {
        return listOf(
            createLinkId("a100_01n"),
            createLinkId("a100_01s"),
            createLinkId("a100_02n"),
            createLinkId("a100_02s"),
            createLinkId("a100_03n"),
            createLinkId("a100_03s"),
            createLinkId("a100_04n"),
            createLinkId("a100_04s")
        )
    }
}
