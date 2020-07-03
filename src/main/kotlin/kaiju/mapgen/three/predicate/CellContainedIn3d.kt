package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d
import kaiju.math.geom.Container3d


fun <T> containedIn(room: Container3d) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    room.contains(x, y, z)
}
