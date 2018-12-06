package kaiju.pathfinder

import kaiju.math.Matrix2d
import kaiju.math.Matrix3d
import kaiju.math.Vec2
import kaiju.math.Vec3
import kaiju.math.getEuclideanDistance
import org.junit.Ignore
import org.junit.Test

// Multi agent pathfinding in time
class TemporalPathfinding {

    @Ignore("should work on this soon")
    @Test
    fun testPathfindingInOpen3d() {

        // If blocks are 6.5 * 6.5 inches
        // xSize = 50
        // ySize = 100
        val mover1 = Mover(
                start = Vec2(1, 1),
                end = Vec2(19, 29))

        val mover2 = Mover(
                start = Vec2(1, 29),
                end = Vec2(29, 1))

        val map = Matrix2d(30, 30) { x, y -> false }

        val paths = findPathTemporal(
                size = Vec2[20, 20],
                timeLimit = 20,
                cost = { pos -> 1.0 },
                distance = { one, two -> getEuclideanDistance(one, two) },
                neighbors = { it: Vec3 ->
                    // get von neuman neighborhood 1 unit in the future
                    it.vonNeumanNeighborhood()
                },
                movers = arrayOf(
                        mover1,
                        mover2
                )
        )

        with(paths[mover1]) {
            assert(this?.first()?.getXY() == mover1.start)
            assert(this?.last()?.getXY() == mover1.end)
        }

        with(paths[mover2]) {
            assert(this?.first()?.getXY() == mover2.start)
            assert(this?.last()?.getXY() == mover2.end)
        }

    }

    private fun findPathTemporal(
            size: Vec2,
            timeLimit: Int,
            cost: (Vec3) -> Double,
            distance: (Vec2, Vec2) -> Double,
            neighbors: (Vec3) -> List<Vec3>,
            movers: Array<Mover>): Map<Mover, Array<Vec3>> {

        // create a temporalMap of future blocked positions

        val map = Matrix3d(size.x, size.y, timeLimit) { x, y, z -> false }


        return mapOf()

    }
}

class Mover(val start: Vec2, val end: Vec2)