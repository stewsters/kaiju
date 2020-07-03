package kaiju.mapgen.two.predicate

import kaiju.math.Matrix2d


fun <T> cellEquals(match: T) = { map: Matrix2d<T>, x: Int, y: Int ->
    map[x, y] == match
}
