package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d


fun <T> not(predicate: (map: Matrix3d<T>, x: Int, y: Int, z: Int) -> Boolean) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    !predicate(map, x, y, z)
}