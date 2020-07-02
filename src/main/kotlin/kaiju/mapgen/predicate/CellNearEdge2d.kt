package mapgen.predicate

import kaiju.math.Matrix2d


fun <T> cellNearEdge() = { map: Matrix2d<T>, x: Int, y: Int ->
    x == 0 || y == 0 || x >= map.xSize - 1 || y >= map.ySize - 1
}
