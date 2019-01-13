package kaiju.math

import junit.framework.TestCase.assertEquals
import org.junit.Test


class MathTest {

    @Test
    fun test2dMath() {
        for (x in -4..35) {
            for (y in -4..35) {

                // println("$x $y :" + Vec2[x, y].toString())
                assert("($x, $y)" == Vec2[x, y].toString())
            }
        }
    }


    @Test
    fun test3dMath() {
        for (x in -4..35) {
            for (y in -4..35) {
                for (z in -4..35) {

                    // println("$x, $y, $z :" + Vec3[x, y, z].toString())
                    assert("($x, $y, $z)" == Vec3[x, y, z].toString())
                }
            }
        }
    }


    @Test
    fun test3dMatrix() {

        val matrix = Matrix3d(10, 10, 10) { x, y, z -> Vec3[x, y, z] }

        for (x in 0 until 10) {
            for (y in 0 until 10) {
                for (z in 0 until 10) {

                    // println("$x, $y, $z :" + matrix[x, y, z].toString())
                    assert(matrix[x, y, z] == Vec3[x, y, z])
                }
            }
        }

    }


    @Test
    fun testNeighbors() {
        val neighbors = Vec2[3, 3].vonNeumanNeighborhood()

        assert(neighbors.size == 4)
        assert(neighbors.contains(Vec2[3, 4]))
        assert(neighbors.contains(Vec2[3, 2]))
        assert(neighbors.contains(Vec2[2, 3]))
        assert(neighbors.contains(Vec2[4, 3]))
    }


    @Test
    fun testLimit() {
        assertEquals(5, 5.limit(3, 6))
        assertEquals(6.0, 5.0.limit(6.0, 7.0))
        assertEquals(5L, 10L.limit(-5L, 5L))
    }
}