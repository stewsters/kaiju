package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d

fun <T> and(vararg predicates: (map: Matrix3d<T>, x: Int, y: Int, z: Int) -> Boolean) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    predicates.all { predicate ->
        predicate(map, x, y, z)
    }
}
