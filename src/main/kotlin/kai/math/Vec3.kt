package kai.math

data class Vec3(val x: Int, val y: Int, val z: Int) {

    operator fun plus(dir: Vec3): Vec3 = get(x + dir.x, y + dir.y, z + dir.z)
    operator fun plus(dir: Facing): Vec3 = get(x + dir.offset.x, y + dir.offset.y, z)

    operator fun minus(dir: Vec3): Vec3 = get(x - dir.x, y - dir.y, z - dir.z)

    fun down(): Vec3 = get(x, y, z - 1)
    fun up(): Vec3 = get(x, y, z + 1)

    companion object {

        // how far off the standard size we will have cached.
        // These happen if you get negative movements or move off the board
        private val offset = 4
        private val size: Int = 128
        private val actualSize = size + 2 * offset
        private val pool = Array((actualSize * actualSize * actualSize), { i ->
            Vec3(
                    i % actualSize - offset,
                    (i % (actualSize * actualSize)) / actualSize - offset,
                    i / (actualSize * actualSize) - offset
            )
        })

        operator fun get(x: Int, y: Int, z: Int): Vec3 {

            val xA = x + offset
            val yA = y + offset
            val zA = z + offset

            return if (xA >= 0 && xA < actualSize && yA >= 0 && yA < actualSize && zA >= 0 && zA < actualSize) {
                pool[zA * actualSize * actualSize + actualSize * yA + xA]
            } else {
                // return a generated one.  This should not happen in practice, but its nice not to throw a bug for the
                // occasional one.  May want to actually error once this gets going
                println("new $x $y $z")
                Vec3(x, y, z)
            }
        }
    }

    fun vonNeumanNeighborhood(): List<Vec3> = listOf(
            Vec3(x, y + 1, z),
            Vec3(x + 1, y, z),
            Vec3(x, y - 1, z),
            Vec3(x - 1, y, z),
            Vec3(z, y, z + 1),
            Vec3(z, y, z - 1)
    )

    fun vonNeumanNeighborhood2d(): List<Vec3> = listOf(
            Vec3(x, y + 1, z),
            Vec3(x + 1, y, z),
            Vec3(x, y - 1, z),
            Vec3(x - 1, y, z)
    )

//    fun mooreNeighborhood(): List<Vec3> = List(8, { index ->
//        if (index >= 4)
//            Vec3[(index + 1) % 2 - 1 + x, (index + 1) / 3 - 1 + y]
//        else
//            Vec3[index % 3 - 1 + x, index / 3 - 1 + y]
//    })

    override fun toString(): String {
        return "($x, $y, $z)"
    }


}

fun getManhattanDistance(pos1: Vec3, pos2: Vec3): Int = getManhattanDistance(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z)

fun getManhattanDistance(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Int = (Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2))


fun getChebyshevDistance(pos1: Vec3, pos2: Vec3): Int = getChebyshevDistance(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z)

fun getChebyshevDistance(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Int = Math.max(Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)), Math.abs(z1 - z2))


fun getEuclideanDistance(pos1: Vec3, pos2: Vec3): Double = getEuclideanDistance(pos1.x.toDouble(), pos1.y.toDouble(), pos1.z.toDouble(), pos2.x.toDouble(), pos2.y.toDouble(), pos2.z.toDouble())
fun getEuclideanDistance(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Double = getEuclideanDistance(x1.toDouble(), y1.toDouble(), z1.toDouble(), x2.toDouble(), y2.toDouble(), z2.toDouble())
fun getEuclideanDistance(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double = Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0) + Math.pow(z1 - z2, 2.0))