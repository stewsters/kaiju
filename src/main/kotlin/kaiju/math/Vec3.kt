package kaiju.math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

data class Vec3(val x: Int, val y: Int, val z: Int) {


    operator fun plus(dir: Vec3): Vec3 = get(x + dir.x, y + dir.y, z + dir.z)
    operator fun plus(dir: Facing): Vec3 = get(x + dir.offset.x, y + dir.offset.y, z)

    operator fun minus(dir: Vec3): Vec3 = get(x - dir.x, y - dir.y, z - dir.z)

    fun down(): Vec3 = get(x, y, z - 1)
    fun up(): Vec3 = get(x, y, z + 1)
    fun getXY(): Vec2 = Vec2[x, y]

    companion object {

        // how far off the standard size we will have cached.
        // These happen if you get negative movements or move off the board
        private const val offset = 4
        private const val size: Int = 32
        private const val actualSize = size + 2 * offset
        private val pool = Array((actualSize * actualSize * actualSize)) { i ->
            Vec3(
                i % actualSize - offset,
                (i % (actualSize * actualSize)) / actualSize - offset,
                i / (actualSize * actualSize) - offset
            )
        }

        operator fun get(x: Int, y: Int, z: Int): Vec3 {

            val xA = x + offset
            val yA = y + offset
            val zA = z + offset

            return if (
                xA in 0 until actualSize &&
                yA in 0 until actualSize &&
                zA in 0 until actualSize
            ) {
                pool[zA * actualSize * actualSize + actualSize * yA + xA]
            } else {
                // return a generated one.  This should not happen in practice, but its nice not to throw a bug for the
                // occasional one.  May want to actually error once this gets going
                Vec3(x, y, z)
            }
        }
    }

    fun inclusiveVonNeumanNeighborhood(): List<Vec3> = listOf(
        this,
        Vec3[x + 1, y, z],
        Vec3[x, y + 1, z],
        Vec3[x, y, z + 1],
        Vec3[x - 1, y, z],
        Vec3[x, y - 1, z],
        Vec3[x, y, z - 1]
    )

    fun vonNeumanNeighborhood(): List<Vec3> = listOf(
        Vec3[x + 1, y, z],
        Vec3[x, y + 1, z],
        Vec3[x, y, z + 1],
        Vec3[x - 1, y, z],
        Vec3[x, y - 1, z],
        Vec3[x, y, z - 1]
    )

    fun vonNeumanNeighborhood2d(): List<Vec3> = listOf(
        Vec3[x, y + 1, z],
        Vec3[x + 1, y, z],
        Vec3[x, y - 1, z],
        Vec3[x - 1, y, z]
    )

    fun inclusiveVonNeumanNeighborhood2d(): List<Vec3> = listOf(
        this,
        Vec3[x, y + 1, z],
        Vec3[x + 1, y, z],
        Vec3[x, y - 1, z],
        Vec3[x - 1, y, z]
    )

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    // Includes the center point
    fun inclusiveMooreNeighborhood2d(): List<Vec3> = List(9) { index ->
        Vec3[index / 3 - 1 + x, index % 3 - 1 + y, z]
    }

    // Excludes center point
    fun mooreNeighborhood2d(): List<Vec3> = List(8) { index ->
        if (index >= 4)
            Vec3[(index + 1) % 3 - 1 + x, (index + 1) / 3 - 1 + y, z]
        else
            Vec3[index % 3 - 1 + x, index / 3 - 1 + y, z]
    }

    fun getMooreNeighborhood(): List<Vec3> = (this + Vec3[0, 0, 1]).inclusiveMooreNeighborhood2d() +
            (this).mooreNeighborhood2d() +
            (this + Vec3[0, 0, -1]).inclusiveMooreNeighborhood2d()

}

fun getManhattanDistance(pos1: Vec3, pos2: Vec3): Int =
    getManhattanDistance(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z)

fun getManhattanDistance(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Int =
    (abs(x1 - x2) + abs(y1 - y2) + abs(z1 - z2))


fun getChebyshevDistance(pos1: Vec3, pos2: Vec3): Int =
    getChebyshevDistance(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z)

fun getChebyshevDistance(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Int =
    max(max(abs(x1 - x2), abs(y1 - y2)), abs(z1 - z2))


fun getEuclideanDistance(pos1: Vec3, pos2: Vec3): Double = getEuclideanDistance(
    pos1.x.toDouble(),
    pos1.y.toDouble(),
    pos1.z.toDouble(),
    pos2.x.toDouble(),
    pos2.y.toDouble(),
    pos2.z.toDouble()
)

fun getEuclideanDistance(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Double =
    getEuclideanDistance(x1.toDouble(), y1.toDouble(), z1.toDouble(), x2.toDouble(), y2.toDouble(), z2.toDouble())

fun getEuclideanDistance(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double =
    sqrt((x1 - x2).pow(2.0) + (y1 - y2).pow(2.0) + (z1 - z2).pow(2.0))