package kaiju.plan

import kaiju.math.Vec2
import org.junit.Test

class DungeonPlanner {

    @Test
    fun test() {

        val startingWorldState = DungeonWorldState()
        val maxCost = 100

        val actions = arrayOf(
                Action(
                        name = "Attack",
                        prerequisite = { w: DungeonWorldState -> w.opponentsHp > 0 && w.player.hp > 0 },
                        effect = { w: DungeonWorldState ->
                            w.opponentsHp--
                            if (w.opponentsHp > 0)
                                w.player.hp--
                            w.cost += 5
                            w
                        }),

                Action(
                        "Stab",
                        { w: DungeonWorldState -> w.opponentsHp > 0 && w.player.hp > 0 && w.hasSword },
                        { w: DungeonWorldState ->
                            w.opponentsHp -= 5
                            if (w.opponentsHp > 0)
                                w.player.hp--
                            w.cost += 10
                            w
                        }),

                Action(
                        "Grab Sword",
                        { w: DungeonWorldState -> w.opponentsHp > 0 && w.player.hp > 0 && !w.hasSword },
                        { w: DungeonWorldState ->
                            w.hasSword = true

                            if (w.opponentsHp > 0)
                                w.player.hp--

                            w.cost += 10
                            w
                        }),
                Action(
                        "Wait",
                        { w: DungeonWorldState -> w.opponentsHp <= 0 },
                        { w: DungeonWorldState ->
                            w.cost += 10
                            w
                        })
        )


        val plan = plan(startingWorldState,
                {
                    if (it.opponentsHp <= 0) {
                        it.player.hp.toFloat()
                    } else {
                        0f
                    }
                },
                actions,
                maxCost
        )

        plan?.forEach({ println(it.name) })

        assert(plan != null)
    }
}

data class Entity(var hp: Int, var pos: Vec2)

// EXAMPLEAGE:

//TODO: Data class allows copy.  I think this can get a lot smaller if we use that.
class DungeonWorldState(
        var player: Entity = Entity(10, Vec2[1, 1]),
        var opponentsHp: Int = 10,
        var hasSword: Boolean = false,

        parentState: DungeonWorldState? = null,
        parentAction: Action<DungeonWorldState>? = null,
        cost: Float = 0f
) : BaseWorldState<DungeonWorldState>(parentState, parentAction, cost), Comparable<DungeonWorldState>, World<DungeonWorldState> {

    constructor(oldWorldState: DungeonWorldState) : this(
            player = oldWorldState.player.copy(),
            opponentsHp = oldWorldState.opponentsHp,
            hasSword = oldWorldState.hasSword,
            cost = oldWorldState.cost,
            parentState = oldWorldState
    )

    override fun getNext(): DungeonWorldState {
        return DungeonWorldState(this)
    }

    override fun compareTo(o: DungeonWorldState): Int {
        return java.lang.Float.compare(cost, o.cost)
    }
}

