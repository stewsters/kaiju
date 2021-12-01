package kaiju.mapgen.three


import kaiju.math.Matrix3d
import kaiju.math.Vec3


/**
 * Fills an area with a border
 *
 * @param map  The map we are working on
 * @param fill The CellType we are filling the center with
 * @param wall The CellType we are filling the edge with
 */
fun <T> fillWithBorder(map: Matrix3d<T>, fill: T, wall: T) {
    map.forEach { x, y, z ->
        if (x == 0 || y == 0 || z == 0 || x >= map.xSize - 1 || y >= map.ySize - 1 || z >= map.zSize - 1) {
            map[x, y, z] = wall
        } else {
            map[x, y, z] = fill
        }
    }
}

fun <T> fill(
    map: Matrix3d<T>,
    predicate: (map: Matrix3d<T>, x: Int, y: Int, z: Int) -> Boolean,
    brush2d: (map: Matrix3d<T>, x: Int, y: Int, z: Int) -> Unit
) {
    map.forEach { x, y, z ->
        if (predicate(map, x, y, z)) {
            brush2d(map, x, y, z)
        }
    }
}

/**
 * Flood fills on things that fit the predicate
 *
 * @param map       The map we are working on
 * @param start     The beginning of the flood fill
 * @param predicate The predicate to check
 * @param brush3d   The brush to fill
 */
fun <T> floodFill(
    map: Matrix3d<T>,
    start: Vec3,
    predicate: (map: Matrix3d<T>, x: Int, y: Int, z: Int) -> Boolean,
    brush3d: (map: Matrix3d<T>, x: Int, y: Int, z: Int) -> Unit
) {
    val todo = mutableListOf<Vec3>()
    val match = mutableListOf<Vec3>()
    val done = HashSet<Vec3>()
    todo.add(start)
    var p: Vec3
    while (todo.isNotEmpty()) {
        p = todo.removeAt(todo.size - 1)
        if (!done.contains(p) && predicate(map, p.x, p.y, p.z)) {
            match.add(p)

            // If we are not going off the edge, add the neighbor
            if (p.x > 0) todo.add(Vec3(p.x - 1, p.y, p.z))
            if (p.x < map.xSize - 1) todo.add(Vec3(p.x + 1, p.y, p.z))
            if (p.y > 0) todo.add(Vec3(p.x, p.y - 1, p.z))
            if (p.y < map.ySize - 1) todo.add(Vec3(p.x, p.y + 1, p.z))
            if (p.z > 0) todo.add(Vec3(p.x, p.y, p.z - 1))
            if (p.z < map.zSize - 1) todo.add(Vec3(p.x, p.y, p.z + 1))
        }
        done.add(p)
    }

    // Goes over the whole map replacing a cell satisfying the predicate with the brush contents.
    for (point3i in match) {
        brush3d(map, point3i.x, point3i.y, point3i.z)
    }
}
