package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d


fun <T> or(vararg predicates: (map: Matrix3d<T>, x: Int, y: Int, z: Int) -> Boolean) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    predicates.any { predicate ->
        predicate(map, x, y, z)
    }
}
