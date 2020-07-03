package kaiju.mapgen.two.predicate

import kaiju.math.Matrix2d


fun <T> or(vararg predicates: (map: Matrix2d<T>, x: Int, y: Int) -> Boolean) = { map: Matrix2d<T>, x: Int, y: Int ->
    predicates.any { predicate ->
        predicate(map, x, y)
    }
}
