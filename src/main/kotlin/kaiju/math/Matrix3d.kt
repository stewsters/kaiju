package kaiju.math

class Matrix3d<T>(val xSize: Int, val ySize: Int, val zSize: Int, val data: Array<T>) {

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

    inline fun forEach(function: (T) -> Unit) = data.forEach(function)

    inline fun forEach(func: (x: Int, y: Int, z: Int) -> Unit) {
        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                for (z in 0 until zSize) {
                    func(x, y, z)
                }
            }
        }
    }

    inline fun forEachIndexed(function: (Int, Int, Int, T) -> Unit) = data.forEachIndexed { i, t ->
        function(
            i % xSize,
            (i % (xSize * ySize)) / xSize,
            i / (xSize * ySize),
            t
        )
    }

    fun getSize() = Vec3(xSize, ySize, zSize)

    fun setFromList(list: List<T>) {
        list.forEachIndexed { index, t -> data[index] = t }
    }

    fun <R : Comparable<R>> sortedBy(function: (T) -> R?): List<T> = data.sortedBy(function)

    inline fun <reified R> map(transform: (T) -> R): Matrix3d<R> = matrix3dOf(xSize, ySize, zSize, data.map(transform))

//    fun submap(boundingRect: RectangularPrism): Matrix3d<T> {
//
//    }

}

inline fun <reified T> matrix3dOf(size: Vec3, init: (Int, Int, Int) -> T) = matrix3dOf(size.x, size.y, size.z, init)

inline fun <reified T> matrix3dOf(xSize: Int, ySize: Int, zSize: Int, init: (Int, Int, Int) -> T) =
    Matrix3d(xSize, ySize, zSize, Array(xSize * ySize * zSize) { i ->
        init(
            i % xSize,
            (i % (xSize * ySize)) / xSize,
            i / (xSize * ySize)
        )
    })

inline fun <reified T> matrix3dOf(xSize: Int, ySize: Int, zSize: Int, dataList: List<T>): Matrix3d<T> =
    Matrix3d(xSize, ySize, zSize, Array(dataList.size) { i -> dataList[i] })

