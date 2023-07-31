package kaiju.plan

import org.junit.Test

class Robotics2018Plan {

    @Test
    fun test() {

        val startingWorldState = Robotics2018WorldState()
        val maxCost = 2.0 * 60.0 + 30 // 2 minutes and 30 seconds

        // This makes us a bit greedy
        fun bonus(time: Double) = ((maxCost - time) / maxCost) / 100.0

        val actions = listOf<Action<Robotics2018WorldState>>(
            Action(
                name = "GetCube",
                prerequisite = { !it.hasBox && it.elevator == ElevatorPos.LOW },
                effect = {
                    Pair(
                        it.copy(
                            hasBox = true
                        ),
                        5.0
                    )
                }),

            Action(
                "PlaceLow",
                { it.hasBox && it.elevator == ElevatorPos.MID && atLow(it.pos) },
                {

                    Pair(
                        it.copy(
                            hasBox = false,
                            boxesPlacedLow = it.boxesPlacedLow + 1 //+ bonus(it.cost)


                        ), 10.0
                    )
                }),

            Action(
                "PlaceHigh",
                { it.hasBox && it.elevator == ElevatorPos.HIGH && atHigh(it.pos) },
                {
                    Pair(
                        it.copy(
                            hasBox = false,
                            boxesPlacedHigh = it.boxesPlacedHigh + 1 // + bonus(it.cost)
                        ),
                        10.0
                    )
                }),
            Action(
                "Raise",
                { it.elevator != ElevatorPos.HIGH },
                {
                    Pair(
                        it.copy(
                            elevator = if (it.elevator == ElevatorPos.LOW)
                                ElevatorPos.MID
                            else
                                ElevatorPos.HIGH
                        ),
                        10.0
                    )
                }),
            Action(
                "Lower",
                { it.elevator != ElevatorPos.LOW },
                {
                    Pair(
                        it.copy(
                            elevator = if (it.elevator == ElevatorPos.HIGH)
                                ElevatorPos.MID
                            else
                                ElevatorPos.LOW
                        ),
                        10.0
                    )
                })
        )


        val plan = plan(
            startingState = startingWorldState,
            fitness = {
                (it.boxesPlacedHigh * 100.0 + it.boxesPlacedLow * 2.0)
            },
            actions = actions,
            maxCost = maxCost
        )

        plan?.forEach { println(it.name) }

        assert(plan != null)
    }


}


private class Vec(var x: Double, var y: Double)
private enum class ElevatorPos {
    LOW,
    MID,
    HIGH
}

private fun atLow(pos: Vec): Boolean {
    return true
}

private fun atHigh(pos: Vec): Boolean {
    return true
}


private data class Robotics2018WorldState(
    var pos: Vec = Vec(2.0, 2.0),
    var hasBox: Boolean = true,
    var elevator: ElevatorPos = ElevatorPos.LOW,

    var boxesStashed: Double = 0.0, // we use doubles to give slight preference to earlier scoring
    var boxesPlacedLow: Double = 0.0,
    var boxesPlacedHigh: Double = 0.0,

    var totalPoints: Int = 0,
)

