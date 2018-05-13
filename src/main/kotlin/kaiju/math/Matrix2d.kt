package kaiju.math

class Matrix2d<T>(val xSize: Int, val ySize: Int, private val data: Array<T>) {

    operator fun get(p: Vec2): T = get(p.x, p.y)

    operator fun get(x: Int, y: Int): T {
        return data[x + y * xSize]
    }

    operator fun set(p: Vec2, value: T) = set(p.x, p.y, value)

    operator fun set(x: Int, y: Int, value: T) {
        data[x + y * xSize] = value
    }

    fun contains(p: Vec2): Boolean = contains(p.x, p.y)

    fun contains(x: Int, y: Int): Boolean = !outside(x, y)

    fun outside(p: Vec2): Boolean = outside(p.x, p.y)

    fun outside(x: Int, y: Int): Boolean = (x < 0 || y < 0 || x >= xSize || y >= ySize)

    fun each(function: (Int, Int, T) -> Unit) = data.forEachIndexed { i, t ->
        function(
                i % xSize,
                i / xSize,
                t
        )
    }

}

inline fun <reified T> Matrix2d(xSize: Int, ySize: Int, init: (Int, Int) -> T) =
        Matrix2d(xSize, ySize, Array(xSize * ySize, { i -> init(i % xSize, i / xSize) }))
