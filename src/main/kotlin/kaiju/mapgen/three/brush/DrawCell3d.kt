package kaiju.mapgen.three.brush

import kaiju.math.Matrix3d


fun <T> drawCell3(value: T) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    map[x, y, z] = value
}