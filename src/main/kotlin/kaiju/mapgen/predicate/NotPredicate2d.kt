package mapgen.predicate

import kaiju.math.Matrix2d


fun <T> not(predicate: (map: Matrix2d<T>, x: Int, y: Int) -> Boolean) = { map: Matrix2d<T>, x: Int, y: Int ->
    !predicate(map, x, y)
}