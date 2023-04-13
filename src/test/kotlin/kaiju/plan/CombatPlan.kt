package kaiju.plan

import org.junit.Test

class TestPlan {

    @Test
    fun test() {

        val startingWorldState = SwordCombatWorldState()
        val maxCost = 100.0

        val actions = listOf(
            Action(
                name = "Punch",
                prerequisite = { w: SwordCombatWorldState -> w.opponentsHp > 0 && w.hp > 0 },
                effect = { w: SwordCombatWorldState ->
                    Pair(
                        w.copy(
                            opponentsHp = w.opponentsHp-1,
                            hp = w.hp - (if(w.opponentsHp>0) 1 else 0)
                        ),
                        5.0
                    )

                }),

            Action(
                "Stab",
                { w: SwordCombatWorldState -> w.opponentsHp > 0 && w.hp > 0 && w.hasSword },
                { w: SwordCombatWorldState ->
                    Pair(
                        w.copy(
                            hp = w.hp + (if (w.opponentsHp > 0) -1 else 0),
                            opponentsHp = w.opponentsHp - 5
                        ),
                        10.0
                    )
                }),

            Action(
                "Grab Sword",
                { w: SwordCombatWorldState -> w.opponentsHp > 0 && w.hp > 0 && !w.hasSword },
                { w: SwordCombatWorldState ->
                    Pair(
                        w.copy(
                            hasSword = true,
                            hp = if (w.opponentsHp > 0) w.hp - 1 else w.hp
                        ),
                        10.0
                    )
                }),
            Action(
                "Le Nap",
                { w: SwordCombatWorldState -> w.opponentsHp <= 0 },
                { w: SwordCombatWorldState ->
                    Pair(w.copy(), 10.0)
                })
        )


        val plan = plan(
            startingWorldState,
            {
                if (it.opponentsHp <= 0) {
                    it.hp.toDouble()
                } else {
                    0.0
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

data class SwordCombatWorldState(
    val hp: Int = 10,
    val opponentsHp: Int = 10,
    val hasSword: Boolean = false
)
