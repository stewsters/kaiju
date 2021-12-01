package kaiju.geom

import kaiju.math.Facing
import kaiju.math.Vec2
import kaiju.math.matrix2dOf
import org.junit.Test

class VecTests {


    @Test
    fun testPoints() {
        for (x in -2..34) {
            for (y in -2..34) {
                val vec2 = Vec2(x, y)

                if (vec2.x != x && vec2.y != y) {
                    println("$x:${vec2.x} $y:${vec2.y}")
                    assert(false)
                }
            }
        }
    }

    @Test
    fun testMoving() {
        val location = Vec2(0, 0) + Facing.EAST + Facing.NORTH

        assert(location.x == 1)
        assert(location.y == 1)
    }

    @Test
    fun testNeighborhood() {

        val center = Vec2(5, 5)
        val neighbors = center.mooreNeighborhood()

        assert(neighbors.size == 8)
        assert(
            neighbors.containsAll(
                listOf(
                    Vec2(4, 4),
                    Vec2(5, 4),
                    Vec2(6, 4),
                    Vec2(4, 5),
                    Vec2(6, 5),
                    Vec2(4, 6),
                    Vec2(5, 6),
                    Vec2(6, 6)
                )
            )
        )

        val neighbors2 = center.inclusiveMooreNeighborhood()
        assert(neighbors2.size == 9)
        assert(
            neighbors2.containsAll(
                listOf(
                    center,
                    Vec2(4, 4),
                    Vec2(5, 4),
                    Vec2(6, 4),
                    Vec2(4, 5),
                    Vec2(6, 5),
                    Vec2(4, 6),
                    Vec2(5, 6),
                    Vec2(6, 6)
                )
            )
        )

    }


    @Test
    fun testMatrix() {
        val mat = matrix2dOf(10, 10) { x, y -> x * y }

        for (x in 0..9) {
            for (y in 0..9) {
                println("$x * $y = ${mat[x, y]}")
            }
        }
    }
}