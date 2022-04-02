package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d

fun <T> nearHorizontalCell(cellType: T) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    var result = false
    for (ix in -1..1) {
        for (iy in -1..1) {
            if (ix == 0 && iy == 0) continue
            if (map.outside(x + ix, y + iy, z)) continue
            if (map[x + ix, y + iy, z] === cellType)
                result = true
        }
    }
    result
}