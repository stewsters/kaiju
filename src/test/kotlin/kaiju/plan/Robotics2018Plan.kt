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
                    it.hasBox = true
                    // TODO: calculate out distance to nearest cube or to slot
                    it.cost += 5
                    it
                }),

            Action(
                "PlaceLow",
                { it.hasBox && it.elevator == ElevatorPos.MID && atLow(it.pos) },
                {
                    it.hasBox = false
                    it.boxesPlacedLow += 1 + bonus(it.cost)
                    it.cost += 10
                    it
                }),

            Action(
                "PlaceHigh",
                { it.hasBox && it.elevator == ElevatorPos.HIGH && atHigh(it.pos) },
                {
                    it.hasBox = false
                    it.boxesPlacedHigh += 1 + bonus(it.cost)
                    it.cost += 10
                    it
                }),
            Action(
                "Raise",
                { it.elevator != ElevatorPos.HIGH },
                {
                    if (it.elevator == ElevatorPos.LOW)
                        it.elevator = ElevatorPos.MID
                    else
                        it.elevator = ElevatorPos.HIGH

                    it.cost += 10
                    it
                }),
            Action(
                "Lower",
                { it.elevator != ElevatorPos.LOW },
                {
                    if (it.elevator == ElevatorPos.HIGH)
                        it.elevator = ElevatorPos.MID
                    else
                        it.elevator = ElevatorPos.LOW

                    it.cost += 10
                    it
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

private fun atStash(pos: Vec): Boolean {
    return true
}


private class Robotics2018WorldState(
    var pos: Vec = Vec(2.0, 2.0),
    var hasBox: Boolean = true,
    var elevator: ElevatorPos = ElevatorPos.LOW,

    var boxesStashed: Double = 0.0, // we use doubles to give slight preference to earlier scoring
    var boxesPlacedLow: Double = 0.0,
    var boxesPlacedHigh: Double = 0.0,

    var totalPoints: Int = 0,

    parentState: Robotics2018WorldState? = null,
    parentAction: Action<Robotics2018WorldState>? = null,
    cost: Double = 0.0
) : BaseWorldState<Robotics2018WorldState>(parentState, parentAction, cost), Comparable<Robotics2018WorldState>,
    World<Robotics2018WorldState> {

    constructor(oldWorldState: Robotics2018WorldState) : this(
        pos = oldWorldState.pos,
        hasBox = oldWorldState.hasBox,
        elevator = oldWorldState.elevator,

        boxesStashed = oldWorldState.boxesStashed,
        boxesPlacedLow = oldWorldState.boxesPlacedLow,
        boxesPlacedHigh = oldWorldState.boxesPlacedHigh,

        totalPoints = oldWorldState.totalPoints,

        cost = oldWorldState.cost,
        parentState = oldWorldState
    )

    override fun getNext(): Robotics2018WorldState {
        return Robotics2018WorldState(this)
    }

    override fun compareTo(o: Robotics2018WorldState): Int {
        return cost.compareTo(o.cost)
    }
}

