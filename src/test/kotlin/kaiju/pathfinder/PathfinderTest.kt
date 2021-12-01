package kaiju.pathfinder

import kaiju.math.*
import kaiju.math.geom.InverseRectangle
import kaiju.math.geom.Rectangle
import org.junit.Test
import kotlin.math.min
import kotlin.math.roundToLong

class PathfinderTest {

    @Test
    fun testPathfinding() {

        val size = Vec2(20, 20)

        val field = Matrix2d(size) { x, y -> x == 6 && y != 0 }
        val path = findPath2d(
                size = field.getSize(),
                cost = { if (field[it]) 100000.0 else 1.0 },
                heuristic = ::getEuclideanDistance,
                neighbors = Vec2::vonNeumanNeighborhood,
                start = Vec2(0, 0),
                end = Vec2(19, 19)
        )

        println(path)

        assert(path!!.contains(Vec2(6, 0)))

    }

    @Test
    fun testPathfindingInOpen2d() {

        // If blocks are 6.5 * 6.5 inches
        // xSize = 50
        // ySize = 100

        val size = Vec2(20, 30)
        val safeDist = 3

        // We should take in a list of autonomous "safe zones" for our allies too
        val obstacles = listOf<Obstacle>(
                Rectangle(Vec2(5, 5), Vec2(15, 10)), // switch
                Rectangle(Vec2(5, 15), Vec2(15, 20)), // scale
                InverseRectangle(Vec2(0, 0), size) // playing field
        )

        // This represents the cost to travel as a scalar field.  Keeping ourselves away from the edges
        // is important, since we are not necessarily accurate, and a default path finder will
        // run you as close to the edge as is mathematically possible.

        val start = Vec2(1, 1)
        val end = Vec2(19, 29)

        val path = findPath2d(
                size,
                { pos ->
                    val d: Double = obstacles.map { it.minDist(pos) }.minOrNull()!!
                    if (d <= 0) {
                        Double.MAX_VALUE
                    } else if (d > safeDist) {
                        1.0
                    } else {
                        // weight spaces near obstacles higher.
                        2 * (1 - (d / safeDist)) + 1
                    }
                },
                { one, two -> 1.0 },
                Vec2::mooreNeighborhood,
                start,
                end
        )


        assert(path != null)
        assert(path?.first() == start)
        assert(path?.last() == end)

    }


    @Test
    fun testPathfindingInOpen3d() {

        val size = Vec3(20, 30, 40)

        val start = Vec3(1, 1, 1)
        val end = Vec3(19, 29, 39)

        val path = findPath3d(
                size,
                { pos -> 1.0 },
                { one, two -> getEuclideanDistance(one, two) },
                Vec3::vonNeumanNeighborhood,
                start,
                end
        )

        assert(path != null)
        assert(path?.first() == start)
        assert(path?.last() == end)
    }

    private fun printField(fieldMap: Matrix2d<Double>, path: List<Vec2>? = null) {
        for (ym in 1..fieldMap.ySize) {
            val y = fieldMap.ySize - ym
            for (x in 0 until fieldMap.xSize) {
                val value = min(fieldMap[x, y].roundToLong(), 9)

                if (path?.contains(Vec2(x, y)) == true) {
                    print("X ")
                } else {
                    print(value.toString() + " ")
                }
            }
            println()
        }

    }
}