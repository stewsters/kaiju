package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d


fun <T> cellEquals(match: T) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    map[x, y, z] == match
}
