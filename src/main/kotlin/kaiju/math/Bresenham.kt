package kaiju.math

import kotlin.math.abs

fun los(start: Vec2, end: Vec2, passable: (x: Int, y: Int) -> Boolean) =
    los(start.x, start.y, end.x, end.y, passable)

fun los(x1: Int, y1: Int, x2: Int, y2: Int, passable: (x: Int, y: Int) -> Boolean): Boolean {
    var x1 = x1
    var y1 = y1
    val dx = abs(x2 - x1)
    val dy = abs(y2 - y1)

    val sx = if (x1 < x2) 1 else -1
    val sy = if (y1 < y2) 1 else -1

    var err = dx - dy

    while (true) {
        if ((x1 == x2 && y1 == y2))
            return true

        if (!passable(x1, y1))
            return false

        if (x1 == x2 && y1 == y2) {
            break
        }

        val e2 = 2 * err

        if (e2 > -dy) {
            err -= dy
            x1 += sx
        }

        if (e2 < dx) {
            err += dx
            y1 += sy
        }
    }
    return true
}