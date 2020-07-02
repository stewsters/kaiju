package mapgen.predicate

import kaiju.math.Matrix2d

fun <T> and(vararg predicates: (map: Matrix2d<T>, x: Int, y: Int) -> Boolean) = { map: Matrix2d<T>, x: Int, y: Int ->
    predicates.all { predicate ->
        predicate(map, x, y)
    }
}
