package mapgen.predicate

import kaiju.math.Matrix2d

fun <T> notNearCell(cellType: T) = { map: Matrix2d<T>, x: Int, y: Int ->
    var result = true
    for (ix in -1..1) {
        for (iy in -1..1) {
            if (ix == 0 && iy == 0) continue
            if (map.outside(x + ix, y + iy)) continue
            if (map.get(x + ix, y + iy) === cellType)
                result = false
        }
    }
    result
}