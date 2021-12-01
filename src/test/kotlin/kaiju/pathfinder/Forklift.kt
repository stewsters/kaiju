package kaiju.pathfinder

import kaiju.math.Vec2
import kaiju.math.getEuclideanDistance
import kaiju.math.matrix2dOf
import org.junit.Test

class Forklift {

    @Test
    fun drivePackages() {

        val passable = matrix2dOf(Vec2(100, 100)) { x, y ->
            x == 0 || y == 0 || x == 39 || y == 39 || x % 2 == 0
        }

        val robots = listOf(
            Robot(0, Vec2(0, 0)),
            Robot(1, Vec2(0, 39)),
            Robot(2, Vec2(39, 0)),
            Robot(3, Vec2(39, 39))
        )

        val validPos = mutableListOf<Vec2>()
        passable.forEachIndexed { x, y, value ->
            if (value) {
                validPos.add(Vec2(x, y))
            }
        }

        val workOrders = (1..100).map { id ->
            Payload(id, validPos.random(), validPos.random())
        }.toMutableList()

        // time passes
        while (true) {

            if (workOrders.isEmpty() && robots.map { it.payload }.filterNotNull().isEmpty()) {
//                println("Work done")
                break
            }

            // each robot does something
            robots.forEach { robot ->

                val payload = robot.payload

                // if we don't have a payload, path to the nearest one.
                if (payload == null) {
//                    println("Robot ${robot.id} looking for new payload")
                    val closest = workOrders.minByOrNull {
                        findPath2d(
                            size = passable.getSize(),
                            cost = { 1.0 },
                            heuristic = { start, end -> getEuclideanDistance(start, end) },
                            neighbors = { it.vonNeumanNeighborhood().filter { passable.contains(it) && passable[it] } },
                            start = robot.position,
                            end = it.location
                        )?.size ?: Integer.MAX_VALUE
                    }

                    if (closest != null) {
                        // if we are on top of it, pick it up, else drive to it
                        if (closest.location == robot.position) {
//                            println("Robot ${robot.id} picked up ${closest.id} at ${robot.position}")
                            robot.plan = null
                            robot.payload = closest
                            workOrders.remove(closest)
                        } else {

                            val plan = findPath2d(
                                size = passable.getSize(),
                                cost = { 1.0 },
                                heuristic = { start, end -> getEuclideanDistance(start, end) },
                                neighbors = {
                                    it.vonNeumanNeighborhood().filter { passable.contains(it) && passable[it] }
                                },
                                start = robot.position,
                                end = closest.location
                            )
                            robot.plan = plan
                            if (plan != null) {
//                                println("Robot ${robot.id}${robot.position} moving towards pickup ${closest.id}${closest.location}")
                                robot.position = plan[1]
                            }
                        }
                    }

                } else {
                    // if we are at the destination, drop off
                    if (payload.destination == robot.position) {
                        // SUCCESS
//                        println("Robot ${robot.id} dropped off ${payload.id} at ${robot.position}")
                        robot.payload = null
                        robot.plan = null
                    } else {
                        // if we have a package, deliver it
                        val plan = findPath2d(
                            size = passable.getSize(),
                            cost = { 1.0 },
                            heuristic = ::getEuclideanDistance,
                            neighbors = { it.vonNeumanNeighborhood().filter { passable.contains(it) && passable[it] } },
                            start = robot.position,
                            end = payload.destination
                        )
                        robot.plan = plan
                        if (plan != null && plan.size > 1) {
//                            println("Robot ${robot.id}${robot.position} moving towards dropoff ${payload.id}${payload.destination}")
                            robot.position = plan[1]
                        }

                    }


                }


            }
        }


        assert(workOrders.isEmpty())
    }

}

class Payload(
    val id: Int,
    val location: Vec2,
    val destination: Vec2
)

const val maxCharge = 100

class Robot(
    val id: Int,
    var position: Vec2,
    var chargeLevel: Int = maxCharge, // number of turns we can take before death
    var plan: List<Vec2>? = null,
    var payload: Payload? = null
)
