package kaiju.plan

import kaiju.math.Vec2
import kaiju.math.geom.Rectangle
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin

class VehiclePlanner {

    val course = Rectangle(
        lower = Vec2(0, 0),
        upper = Vec2(10, 10)
    )

    val centerWidth = Vec2(2, 2)
    val center = course.center()
    val centerPillar = Rectangle(lower = center - centerWidth, upper = center + centerWidth)

    @Test
    fun drive() {
        val startingState = VehicleState(
            momentum = 0.0,
            facing = 0.0,
            pos = Vec2f(course.upper.x / 2.0, course.upper.y / 4.0),
            hasCrashed = false
        )
        val maxCost = 5.0 // this is too small

        val gas = mapOf(
            "break" to -1,
//            "coast" to 0,
            "accel" to 1
        )
        val steer = mapOf(
            "left" to Math.toRadians(-30.0),
            "straight" to 0.0,
            "right" to Math.toRadians(30.0)
        )


        val actions: List<Action<VehicleState>> = gas.flatMap { g ->
            steer.map { s ->
                Action<VehicleState>(
                    name = "$g $s",
                    prerequisite = { w -> !w.hasCrashed },
                    effect = { vs ->
                        val m = vs.momentum + g.value
                        val f = vs.facing + s.value
                        val p = Vec2f(
                            vs.pos.x + m * cos(f),
                            vs.pos.y + m * sin(f)
                        )
                        val c = !course.contains(p) || centerPillar.contains(p)

                        Pair(
                            VehicleState(
                                momentum = m,
                                facing = f,
                                pos = p,
                                hasCrashed = vs.hasCrashed || c
                            ),
                            1.0
                        )
                    }
                )
            }
        }

        val plan = plan(
            startingState = startingState,
            fitness = { vs -> vs.momentum},
            actions = actions,
            maxCost = maxCost
        )

        plan?.forEach { println(it.name) }

        assert(plan != null)

    }
}

private fun Rectangle.contains(p: Vec2f): Boolean =
    this.lower.x < p.x && this.lower.y < p.y &&
            this.upper.x > p.x && this.upper.y > p.y


data class VehicleState(
    val momentum: Double,
    val facing: Double,
    val pos: Vec2f,
    val hasCrashed: Boolean
)

class Vec2f(val x: Double, val y: Double)
