package kaiju.pathfinder

import kaiju.math.Vec2
import kaiju.math.Vec3
import kaiju.math.min
import org.junit.Test

class DijkstraMapTest {

    @Test
    fun test2d() {

        val size = Vec2[10, 10]
        val source = Vec2[5, 5]

        val map = dijkstraMap2d(size, listOf(source), { pos -> pos.x > 6 })

        for (y in (0 until map.ySize)) {
            println()
            for (x in (0 until map.xSize)) {
                print(min(map[x, y], 9))
            }
        }

        // we should be able to follow it back to the source.

        var done = false
        var pos = Vec2[0, 0]

        // For each position, find one that's better and go there until we get to a source
        while (!done) {

            val betterPos = pos.inclusiveMooreNeighborhood()
                    .filter { map.contains(it) }
                    .minBy { map[it] }

            if (betterPos != null && betterPos != pos) {
                //set it
                pos = betterPos
            } else {
                done = true
            }
        }

        assert(pos == source)
    }


    @Test
    fun test3d() {

        val size = Vec3[10, 10, 10]
        val source = Vec3[5, 5, 5]

        val map = dijkstraMap3d(size, listOf(source), { pos -> pos.x > 6 })

//        for (y in (0 until map.ySize)) {
//
//            for (x in (0 until map.xSize)) {
//                for (x in (0 until map.xSize)) {
//                print(min(map[x, y], 9))
//            }
//        }

        // we should be able to follow it back to the source.

        var done = false
        var pos = Vec3[0, 0, 0]

        // For each position, find one that's better and go there until we get to a source
        while (!done) {

            val betterPos = pos.inclusiveVonNeumanNeighborhood()
                    .filter { map.contains(it) }
                    .minBy { map[it] }

            if (betterPos != null && betterPos != pos) {
                //set it
                pos = betterPos
            } else {
                done = true
            }
        }

        assert(pos == source)
    }
}