package kaiju.mapgen.two.predicate

import kaiju.math.Matrix2d


fun <T> cellEqualsAny(types: List<T>) = { map: Matrix2d<T>, x: Int, y: Int ->
    types.contains(map[x, y])
}
