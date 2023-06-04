package org.matsim.analysis

import org.locationtech.jts.geom.Geometry
import org.matsim.api.core.v01.Id
import org.matsim.api.core.v01.population.Activity
import org.matsim.api.core.v01.population.Person
import org.matsim.core.population.PopulationUtils
import org.matsim.core.utils.geometry.geotools.MGC
import org.matsim.core.utils.geometry.transformations.TransformationFactory
import org.matsim.core.utils.gis.ShapeFileReader
import java.io.*

fun main() {
    val affectedAgents: MutableSet<Id<Person>> = HashSet()
    val affectedAgentsDistricts: MutableMap<String, Int> = HashMap()
    BufferedReader(FileReader("./affected_agents.csv")).use { br ->
        var line: String?
        while (br.readLine().also { line = it } != null) {
            val values = line!!.split(",").toTypedArray()
            affectedAgents.add(Id.createPersonId(values[0]))
        }
    }

    val features = ShapeFileReader.getAllFeatures("./Bezirke_-_Berlin/Berlin_Bezirke.shp")
    for (feature in features) {
        affectedAgentsDistricts[feature.getAttribute("Gemeinde_n").toString()] = 0
    }
    val transformation = TransformationFactory.getCoordinateTransformation("EPSG:31468", "EPSG:3857")
    val population =
        PopulationUtils.readPopulation("./scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct/berlin-v5.5-1pct.output_plans.xml.gz")

    try {
        BufferedWriter(FileWriter("affected_agents_home.csv")).use { writer ->
            writer.write("agent_id,home_x,home_y")
            writer.newLine()

            for (person in population.persons.values) {
                if (affectedAgents.contains(person.id)) {
                    val plan = person.selectedPlan
                    val firstElement = plan.planElements[0]
                    val activity = firstElement as Activity

                    if (activity.type != "freight") {
                        val activityCoord = activity.coord
                        val transformedAActivityCoord = transformation.transform(activityCoord)
                        val activityPoint = MGC.coord2Point(transformedAActivityCoord)

                        for (feature in features) {
                            val geo = feature.defaultGeometry as Geometry
                            if (geo.contains(activityPoint)) {
                                affectedAgentsDistricts[feature.getAttribute("Gemeinde_n").toString()] =
                                    affectedAgentsDistricts[feature.getAttribute("Gemeinde_n").toString()]!! + 1
                            }
                        }

                        val line = "${person.id},${activityCoord.x},${activityCoord.y}"
                        writer.write(line)
                        writer.newLine()
                    }
                }
            }

            println("Write to file completed successfully.")
        }
    } catch (e: IOException) {
        System.err.println("Error writing to file: " + e.message)
    }

    try {
        BufferedWriter(FileWriter("affected_agents_districts.csv")).use { writer ->
            writer.write("district_name,count")
            writer.newLine()

            for (k in affectedAgentsDistricts.keys) {
                writer.write("${k},${affectedAgentsDistricts[k]}")
                writer.newLine()
            }

            println("Write to file completed successfully.")
        }
    } catch (e: IOException) {
        System.err.println("Error writing to file: " + e.message)
    }
}
