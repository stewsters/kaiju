

package kaiju.mapgen.two


import kaiju.math.Matrix2d
import kaiju.math.Vec2


/**
 * Fills an area with a border
 *
 * @param map  The map we are working on
 * @param fill The CellType we are filling the center with
 * @param wall The CellType we are filling the edge with
 */
fun <T> fillWithBorder(map: Matrix2d<T>, fill: T, wall: T) {
    map.forEach { x, y ->
        if (x == 0 || y == 0 || x >= map.xSize - 1 || y >= map.ySize - 1) {
            map[x, y] = wall
        } else {
            map[x, y] = fill
        }
    }
}

fun <T> fill(
    map: Matrix2d<T>,
    predicate: (map: Matrix2d<T>, x: Int, y: Int) -> Boolean,
    brush2d: (map: Matrix2d<T>, x: Int, y: Int) -> Unit
) {
    map.forEach { x, y ->
        if (predicate(map, x, y)) {
            brush2d(map, x, y)
        }
    }
}

/**
 * Flood fills on things that fit the predicate
 *
 * @param map       The map we are working on
 * @param start     The beginning of the flood fill
 * @param predicate The predicate to check
 * @param brush2d   The brush to fill
 */
fun <T> floodFill(
    map: Matrix2d<T>,
    start: Vec2,
    predicate: (map: Matrix2d<T>, x: Int, y: Int) -> Boolean,
    brush2d: (map: Matrix2d<T>, x: Int, y: Int) -> Unit
) {
    val todo = mutableListOf<Vec2>()
    val match = mutableListOf<Vec2>()
    val done = HashSet<Vec2>()
    todo.add(start)
    var p: Vec2
    while (todo.isNotEmpty()) {
        p = todo.removeAt(todo.size - 1)
        if (!done.contains(p) && predicate(map, p.x, p.y)) {
            match.add(p)

            if (p.x > 0) todo.add(Vec2(p.x - 1, p.y))
            if (p.x < map.xSize - 1) todo.add(Vec2(p.x + 1, p.y))
            if (p.y > 0) todo.add(Vec2(p.x, p.y - 1))
            if (p.y < map.ySize - 1) todo.add(Vec2(p.x, p.y + 1))
        }
        done.add(p)
    }

    // Goes over the whole map replacing a cell satisfying the predicate with the brush contents.
    for (point2i in match) {
        brush2d(map, point2i.x, point2i.y)
    }
}
