package kaiju.math.geom

import kaiju.math.Vec2


class InverseRectangle(lower: Vec2, upper: Vec2) : Rectangle(lower, upper) {

    override fun minDist(point: Vec2): Double {

        return if (outside(point)) {
            0.0
        } else {
            Math.min(
                    Math.min(point.x.toDouble() - lower.x + 1, point.y.toDouble() - lower.y + 1),
                    Math.min(upper.x - point.x.toDouble(), upper.y - point.y.toDouble())
            )
        }
    }
}

