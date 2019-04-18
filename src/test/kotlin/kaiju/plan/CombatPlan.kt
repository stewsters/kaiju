package kaiju.plan

import org.junit.Test

class TestPlan {

    @Test
    fun test() {

        val startingWorldState = SwordCombatWorldState()
        val maxCost = 100

        val actions = arrayOf(
                Action(
                        name = "Punch",
                        prerequisite = { w: SwordCombatWorldState -> w.opponentsHp > 0 && w.hp > 0 },
                        effect = { w: SwordCombatWorldState ->
                            w.opponentsHp--
                            if (w.opponentsHp > 0)
                                w.hp--
                            w.cost += 5
                            w
                        }),

                Action(
                        "Stab",
                        { w: SwordCombatWorldState -> w.opponentsHp > 0 && w.hp > 0 && w.hasSword },
                        { w: SwordCombatWorldState ->
                            w.opponentsHp -= 5
                            if (w.opponentsHp > 0)
                                w.hp--
                            w.cost += 10
                            w
                        }),

                Action(
                        "Grab Sword",
                        { w: SwordCombatWorldState -> w.opponentsHp > 0 && w.hp > 0 && !w.hasSword },
                        { w: SwordCombatWorldState ->
                            w.hasSword = true

                            if (w.opponentsHp > 0)
                                w.hp--

                            w.cost += 10
                            w
                        }),
                Action(
                        "Le Nap",
                        { w: SwordCombatWorldState -> w.opponentsHp <= 0 },
                        { w: SwordCombatWorldState ->
                            w.cost += 10
                            w
                        })
        )


        val plan = plan(startingWorldState,
                {
                    if (it.opponentsHp <= 0) {
                        it.hp.toFloat()
                    } else {
                        0f
                    }
                },
                actions,
                maxCost
        )

        plan?.forEach { println(it.name) }

        assert(plan != null)
    }
}


// EXAMPLE:

private class SwordCombatWorldState(
        var hp: Int = 10,
        var opponentsHp: Int = 10,
        var hasSword: Boolean = false,
        parentState: SwordCombatWorldState? = null,
        parentAction: Action<SwordCombatWorldState>? = null,
        cost: Float = 0f
) : BaseWorldState<SwordCombatWorldState>(parentState, parentAction, cost), Comparable<SwordCombatWorldState>, World<SwordCombatWorldState> {

    constructor(oldWorldState: SwordCombatWorldState) : this(
            hp = oldWorldState.hp,
            opponentsHp = oldWorldState.opponentsHp,
            hasSword = oldWorldState.hasSword,
            cost = oldWorldState.cost,
            parentState = oldWorldState
    )

    override fun getNext(): SwordCombatWorldState {
        return SwordCombatWorldState(this)
    }

    override fun compareTo(other: SwordCombatWorldState): Int {
        return java.lang.Float.compare(cost, other.cost)
    }
}

