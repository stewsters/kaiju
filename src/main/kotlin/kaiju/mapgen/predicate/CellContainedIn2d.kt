package kaiju.mapgen.predicate

import kaiju.math.Container2d
import kaiju.math.Matrix2d


fun <T> containedIn(room: Container2d) = { map: Matrix2d<T>, x: Int, y: Int ->
    room.contains(x, y)
}
