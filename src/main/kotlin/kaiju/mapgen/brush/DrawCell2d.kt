import kaiju.math.Matrix2d


fun <T> drawCell(value: T) = { generatedMap2d: Matrix2d<T>, x: Int, y: Int ->
    generatedMap2d[x, y] = value
}