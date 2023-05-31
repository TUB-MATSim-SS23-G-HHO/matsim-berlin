package org.matsim.analysis

import org.matsim.core.events.EventsUtils
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException

fun main() {
    val handler = FindAffectedAgentsHandler()
    val manager = EventsUtils.createEventsManager()
    manager.addHandler(handler)

    EventsUtils.readEvents(
        manager,
        "./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct/berlin-v5.5-1pct.output_events.xml.gz"
    )

    try {
        BufferedWriter(FileWriter("affected_agents_count.csv")).use { writer ->
            // Write the header line to the CSV file
            writer.write("total_agents_count,affected_agents_count")
            writer.newLine()

            val totalAgents = handler.getAgents().size
            val affectedAgentsCount = handler.getA100Users().size
            writer.write("$totalAgents,$affectedAgentsCount")
            writer.newLine()

            println("Write to file completed successfully.")
        }
    } catch (e: IOException) {
        System.err.println("Error writing to file: " + e.message)
    }

    try {
        BufferedWriter(FileWriter("affected_agents.csv")).use { writer ->
            // Write the header line to the CSV file
            writer.write("agents_id")
            writer.newLine()

            for (agentId in handler.getA100Users()) {
                writer.write(agentId.toString())
                writer.newLine()
            }

            println("Write to file completed successfully.")
        }
    } catch (e: IOException) {
        System.err.println("Error writing to file: " + e.message)
    }
}
