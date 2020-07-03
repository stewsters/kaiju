package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d

fun <T> notNearCell(cellType: T) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    var result = true
    for (ix in -1..1) {
        for (iy in -1..1) {
            for (iz in -1..1) {
                if (ix == 0 && iy == 0 && iz == 0) continue
                if (map.outside(x + ix, y + iy, z + iz)) continue
                if (map[x + ix, y + iy, z + iz] === cellType)
                    result = false
            }
        }
    }
    result
}