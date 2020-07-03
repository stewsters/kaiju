package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d


fun <T> cellEqualsAny(types: List<T>) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    types.contains(map[x, y, z])
}
