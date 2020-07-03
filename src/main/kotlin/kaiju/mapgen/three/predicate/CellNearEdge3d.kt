package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d


fun <T> cellNearEdge() = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    x == 0 || y == 0 || z == 0 || x >= map.xSize - 1 || y >= map.ySize - 1 || z >= map.zSize - 1
}
