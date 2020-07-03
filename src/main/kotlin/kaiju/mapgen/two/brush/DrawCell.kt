package kaiju.mapgen.two.brush

import kaiju.math.Matrix2d


fun <T> drawCell(value: T) = { map: Matrix2d<T>, x: Int, y: Int ->
    map[x, y] = value
}