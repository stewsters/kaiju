package kaiju.plan

import java.util.*
import kotlin.collections.ArrayList


class Action<W>(val name: String, val prerequisite: (W) -> Boolean, val effect: (W) -> W)


fun <W> plan(
    startingState: W,
    fitness: (W) -> Float,
    actions: Array<Action<W>>,
    maxCost: Int
): List<Action<W>>? where W : World<W> {

    val endState = ArrayList<W>()

    val openList = PriorityQueue<W>()
    openList.add(startingState)

    // while we have a world state in the open
    while (openList.size > 0) {
        val current = openList.poll()
        val viableActions = actions.filter {
            it.prerequisite(current)
        }

        if (viableActions.isEmpty()) {
            endState.add(current)
            continue
        }

        viableActions.forEach { action ->
            val next: W = action.effect(current.getNext())
            next.parentAction = action
            next.parentState = current
            if (next.cost <= maxCost) {
                openList.add(next)
            } else {
                endState.add(next)
            }
        }
    }

    var state = endState.maxByOrNull { fitness(it) }

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

interface World<W> {

    var cost: Float
    var parentState: W?
    var parentAction: Action<W>?

    fun getNext(): W

}

abstract class BaseWorldState<W>(
    override var parentState: W? = null,
    override var parentAction: Action<W>? = null,
    override var cost: Float = 0f
) : World<W>


