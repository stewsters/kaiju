package kaiju.math

data class Vec2(val x: Int, val y: Int) {

    operator fun plus(dir: Vec2): Vec2 = get(x + dir.x, y + dir.y)
    operator fun plus(dir: Facing): Vec2 = get(x + dir.offset.x, y + dir.offset.y)

    operator fun minus(dir: Vec2): Vec2 = get(x - dir.x, y - dir.y)
    operator fun minus(dir: Facing): Vec2 = get(x - dir.offset.x, y - dir.offset.y)

    companion object {

        // how far off the standard size we will have cached.
        // These happen if you get negative movements or move off the board
        private const val offset = 4
        private val size: Int = 32
        private val actualSize = size + 2 * offset
        private val pool = Array((actualSize * actualSize)) { i -> Vec2(i % (actualSize) - offset, i / actualSize - offset) }

        operator fun get(x: Int, y: Int): Vec2 {

            val xA = x + offset
            val yA = y + offset

            return if (xA >= 0 && xA < actualSize && yA >= 0 && yA < actualSize) {
                pool[actualSize * yA + xA]
            } else {
                // return a generated one.  This should not happen in practice, but its nice not to throw a bug for the
                // occasional one.  May want to actually error once this gets going
                println("new $x $y")
                Vec2(x, y)
            }
        }
    }

    fun vonNeumanNeighborhood(): List<Vec2> = listOf(
            Vec2[x, y + 1],
            Vec2[x + 1, y],
            Vec2[x, y - 1],
            Vec2[x - 1, y]
    )

    fun mooreNeighborhood(): List<Vec2> = List(8, { index ->
        if (index >= 4)
            Vec2[(index + 1) % 3 - 1 + x, (index + 1) / 3 - 1 + y]
        else
            Vec2[index % 3 - 1 + x, index / 3 - 1 + y]
    })

    override fun toString(): String {
        return "($x, $y)"
    }
}

fun getManhattanDistance(pos1: Vec2, pos2: Vec2): Int = getManhattanDistance(pos1.x, pos1.y, pos2.x, pos2.y)

fun getManhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int = (Math.abs(x1 - x2) + Math.abs(y1 - y2))


fun getChebyshevDistance(pos1: Vec2, pos2: Vec2): Int = getChebyshevDistance(pos1.x, pos1.y, pos2.x, pos2.y)

fun getChebyshevDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2))


fun getEuclideanDistance(pos1: Vec2, pos2: Vec2): Double = getEuclideanDistance(pos1.x.toDouble(), pos1.y.toDouble(), pos2.x.toDouble(), pos2.y.toDouble())
fun getEuclideanDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double = Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0))