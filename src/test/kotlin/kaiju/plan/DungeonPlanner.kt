package kaiju.plan

import kaiju.math.Vec2
import org.junit.Test

class DungeonPlanner {

    @Test
    fun test() {

        val startingWorldState = DungeonWorldState()
        val maxCost = 100.0

        val actions = listOf(
            Action(
                name = "Attack",
                prerequisite = { w: DungeonWorldState -> w.opponentsHp > 0 && w.player.hp > 0 },
                effect = { w: DungeonWorldState ->
                    Pair(
                        w.copy(
                            opponentsHp = w.opponentsHp - 1,
                            player = if (w.opponentsHp > 0)
                                w.player.copy(
                                    hp = w.player.hp - 1
                                ) else w.player
                        ),
                        5.0
                    )
                }),

            Action(
                "Stab",
                { w: DungeonWorldState -> w.opponentsHp > 0 && w.player.hp > 0 && w.hasSword },
                { w: DungeonWorldState ->
                    Pair(
                        w.copy(
                            opponentsHp = w.opponentsHp - 5,
                            player = if (w.opponentsHp > 0) w.player.copy(hp = w.player.hp - 1) else w.player
                        ),
                        10.0
                    )
                }),

            Action(
                "Grab Sword",
                { w: DungeonWorldState -> w.opponentsHp > 0 && w.player.hp > 0 && !w.hasSword },
                { w: DungeonWorldState ->
                    Pair(
                        w.copy(
                            hasSword = true,
                            player = if (w.opponentsHp > 0) w.player.copy(hp = w.player.hp - 1) else w.player
                        ), 10.0
                    )
                }),
            Action(
                "Wait",
                { w: DungeonWorldState -> w.opponentsHp <= 0 },
                { w: DungeonWorldState ->
                    Pair(
                        w,
                        10.0
                    )
                })
        )


        val plan = plan(
            startingWorldState,
            {
                if (it.opponentsHp <= 0) {
                    it.player.hp.toDouble()
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

data class Entity(val hp: Int, val pos: Vec2)

data class DungeonWorldState(
    val player: Entity = Entity(10, Vec2(1, 1)),
    val opponentsHp: Int = 10,
    val hasSword: Boolean = false
)

