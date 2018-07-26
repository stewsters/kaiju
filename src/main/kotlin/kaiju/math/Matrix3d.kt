package kaiju.math

class Matrix3d<T>(val xSize: Int, val ySize: Int, val zSize: Int, private val data: Array<T>) {

    operator fun get(p: Vec3): T = get(p.x, p.y, p.z)

    operator fun get(x: Int, y: Int, z: Int): T {
        return data[x + y * xSize + z * xSize * ySize]
    }

    operator fun set(p: Vec3, value: T) = set(p.x, p.y, p.z, value)

    operator fun set(x: Int, y: Int, z: Int, value: T) {
        data[x + y * xSize + z * xSize * ySize] = value
    }

    fun contains(p: Vec3): Boolean = contains(p.x, p.y, p.z)

    fun contains(x: Int, y: Int, z: Int): Boolean = !outside(x, y, z)

    fun outside(p: Vec3): Boolean = outside(p.x, p.y, p.z)

    fun outside(x: Int, y: Int, z: Int): Boolean = (x < 0 || y < 0 || z < 0 || x >= xSize || y >= ySize || z >= zSize)

    fun each(function: (Int, Int, Int, T) -> Unit) = data.forEachIndexed { i, t ->
        function(
                i % xSize,
                (i % (xSize * ySize)) / xSize,
                i / (xSize * ySize),
                t
        )
    }

    fun setFromList(list: List<T>) {
        list.forEachIndexed { index, t -> data[index] = t }
    }

//    fun submap(boundingRect: RectangularPrism): Matrix3d<T> {
//
//    }

}

inline fun <reified T> Matrix3d(size: Vec3, init: (Int, Int, Int) -> T) = Matrix3d(size.x, size.y, size.z, init)

inline fun <reified T> Matrix3d(xSize: Int, ySize: Int, zSize: Int, init: (Int, Int, Int) -> T) =
        Matrix3d(xSize, ySize, zSize, Array(xSize * ySize * zSize) { i ->
            init(
                    i % xSize,
                    (i % (xSize * ySize)) / xSize,
                    i / (xSize * ySize)
            )
        })

