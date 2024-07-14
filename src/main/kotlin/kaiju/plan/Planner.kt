package kaiju.plan

import kaiju.datastructure.PriorityQueue


// TODO:
// add a test that sees how far we can go before running out of memory before these:
// remove duplicate world states, these can cause loops -> wasted search
// add a heuristic
// hierarchy?

fun <W> plan(
    startingState: W,
    fitness: (W) -> Double, // How good that world state is
    actions: List<Action<W>>, // ways in which the world state can change
    maxCost: Double // max cost we are searching.  Usually this is used for time
): List<Action<W>>? {

    val endState = ArrayList<World<W>>()

    val openList = PriorityQueue<World<W>> { w1: World<W>, w2: World<W> -> w1.compareTo(w2) }
    openList.add(World(startingState, 0.0, parentAction = null, parentState = null))

    // while we have a world state in the open
    while (openList.size > 0) {
        val current = openList.poll() ?: continue
        val viableActions = actions.filter {
            it.prerequisite(current.w)
        }

        if (viableActions.isEmpty()) {
            endState.add(current)
            continue
        }

        viableActions.forEach { action ->
            val (world, cost) = action.effect(current.w)
            val next = World(
                w = world,
                cost = current.cost + cost,
                parentAction = action,
                parentState = current,
            )
            if (next.cost <= maxCost) {
                openList.add(next)
            } else {
                endState.add(next)
            }
        }
    }

    var state = endState.maxByOrNull { fitness(it.w) }

    if (state == null) {
        return null
    }

    val plan = ArrayList<Action<W>>()

    while (state?.parentAction != null && state.parentState != null) {
        plan.add(state.parentAction!!)
        state = state.parentState
    }

    plan.reverse()

    return plan
}

class Action<W>(
    val name: String,
    val prerequisite: (W) -> Boolean,
    val effect: (W) -> Pair<W, Double>
)

class World<W>(
    var w: W,
    var cost: Double,
    var parentState: World<W>?,
    var parentAction: Action<W>?
) : Comparable<World<W>> {
    override fun compareTo(other: World<W>): Int {
        return cost.compareTo(other.cost)
    }
}
