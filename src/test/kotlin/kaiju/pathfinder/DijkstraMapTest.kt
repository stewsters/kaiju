package kaiju.pathfinder

import kaiju.math.Vec2
import kaiju.math.min
import org.junit.Test

class DijkstraMapTest() {


    @Test
    fun test() {

        val map = dijkstraMap2d(Vec2[10, 10], listOf(Vec2[5, 5]), { pos -> pos.x > 6 })

        for (y in (0 until map.ySize)) {
            println()
            for (x in (0 until map.xSize)) {


                print(min(map[x, y], 9))
            }
        }
    }
}