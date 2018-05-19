package kaiju.geom

import kaiju.math.Facing
import kaiju.math.Matrix2d
import kaiju.math.Vec2
import org.junit.Test
import java.util.*

class VecTests {


    @Test
    fun testPoints() {
        for (x in -2..34) {
            for (y in -2..34) {
                val vec2 = Vec2[x, y]

                if (vec2.x != x && vec2.y != y) {
                    println("$x:${vec2.x} $y:${vec2.y}")
                    assert(false)
                }
            }
        }
    }

    @Test
    fun testMoving() {
        val location = Vec2[0, 0] + Facing.EAST + Facing.NORTH

        assert(location.x == 1)
        assert(location.y == 1)
    }

    @Test
    fun testNeighborhood() {

        val center = Vec2[5, 5]
        val neighbors = center.mooreNeighborhood()

        assert(neighbors.size == 8)

        neighbors.forEach { println(it) }

        assert(neighbors.containsAll(Arrays.asList(
                Vec2[4, 4],
                Vec2[5, 4],
                Vec2[6, 4],
                Vec2[4, 5],
                Vec2[6, 5],
                Vec2[4, 6],
                Vec2[5, 6],
                Vec2[6, 6]
        ))
        )
    }


    @Test
    fun testMatrix() {
        val mat = Matrix2d(10, 10, { x, y -> x * y })

        for (x in 0..9) {
            for (y in 0..9) {
                println("$x * $y = ${mat[x, y]}")
            }
        }
    }
}