package kaiju.mapgen.two.predicate

import kaiju.math.Matrix2d


fun <T> nearCell(cellType: T) = { map: Matrix2d<T>, x: Int, y: Int ->
    var result = false
    for (ix in -1..1) {
        for (iy in -1..1) {
            if (ix == 0 && iy == 0) continue
            if (map.outside(x + ix, y + iy)) continue
            if (map[x + ix, y + iy] === cellType)
                result = true
        }
    }
    result

}