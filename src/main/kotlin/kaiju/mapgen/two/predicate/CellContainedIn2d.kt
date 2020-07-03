package kaiju.mapgen.two.predicate

import kaiju.math.Matrix2d
import kaiju.math.geom.Container2d


fun <T> containedIn(room: Container2d) = { map: Matrix2d<T>, x: Int, y: Int ->
    room.contains(x, y)
}
