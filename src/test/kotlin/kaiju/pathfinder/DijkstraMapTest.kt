package kaiju.pathfinder

import kaiju.math.Vec2
import org.junit.Test

class DijkstraMapTest() {


    @Test
    fun test() {

        val map = dijkstraMap2d(Vec2[8, 8], listOf(Vec2[4, 4]))

        for (y in (0 until map.ySize)) {
            println()
            for (x in (0 until map.xSize)) {

                print(map[x, y])
            }
        }
    }
}