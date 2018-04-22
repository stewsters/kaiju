package kai.math

import java.lang.Math.abs

open class Rectangle(val lower: Vec2, val upper: Vec2) : Obstacle {

    fun inside(point: Vec2): Boolean {
        return point.x >= lower.x && point.y >= lower.y && point.x <= upper.x && point.y <= upper.y
    }

    fun outside(point: Vec2): Boolean {
        return point.x < 0 || point.y < 0 || point.x > upper.x || point.y > upper.y
    }

    override fun minDist(point: Vec2): Double {
        if (inside(point)) {
            return 0.0

        } else if (point.x >= lower.x && point.x <= upper.x) {
            // X is centered
            return if (point.y > upper.y) {
                abs(point.y.toDouble() - upper.y.toDouble())
            } else {
                abs(point.y.toDouble() - lower.y.toDouble())
            }
        } else if (point.y >= lower.y && point.y <= upper.y) {
            // Y is centered
            return if (point.x > lower.x) {
                abs(point.x.toDouble() - upper.x.toDouble())
            } else {
                abs(point.x.toDouble() - lower.x.toDouble())
            }
        } else if (point.x < lower.x && point.y > upper.y) { // upper left
            return getEuclideanDistance(point, Vec2[lower.x, upper.y])

        } else if (point.x > upper.x && point.y > upper.y) { // upper right
            return getEuclideanDistance(point, Vec2[upper.x, upper.y])

        } else if (point.x > upper.x && point.y < lower.y) { // lower right
            return getEuclideanDistance(point, Vec2[upper.x, lower.y])

        } else if (point.x < lower.x && point.y < lower.y) { // lower left
            return getEuclideanDistance(point, Vec2[lower.x, lower.y])

        } else {
            return -1.0//throw Exception("That's not good")
        }
    }
}

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


open class RectangularPrism(val lower: Vec3, val upper: Vec3) {

    fun inside(point: Vec3): Boolean {
        return point.x >= lower.x && point.y >= lower.y && point.y >= lower.z
                && point.x <= upper.x && point.y <= upper.y && point.z <= upper.z
    }

    fun outside(point: Vec3): Boolean {
        return point.x < 0 || point.y < 0 || point.z < 0 || point.x > upper.x || point.y > upper.y || point.z > upper.z
    }

//    override fun minDist(point: Vec3): Double {
//        if (inside(point)) {
//            return 0.0
//
//        } else if (point.x >= lower.x && point.x <= upper.x) {
//            // X is centered
//            return if (point.y > upper.y) {
//                abs(point.y.toDouble() - upper.y.toDouble())
//            } else {
//                abs(point.y.toDouble() - lower.y.toDouble())
//            }
//        } else if (point.y >= lower.y && point.y <= upper.y) {
//            // Y is centered
//            return if (point.x > lower.x) {
//                abs(point.x.toDouble() - upper.x.toDouble())
//            } else {
//                abs(point.x.toDouble() - lower.x.toDouble())
//            }
//        } else if (point.x < lower.x && point.y > upper.y) { // upper left
//            return getEuclideanDistance(point, Vec3[lower.x, upper.y])
//
//        } else if (point.x > upper.x && point.y > upper.y) { // upper right
//            return getEuclideanDistance(point, Vec3[upper.x, upper.y])
//
//        } else if (point.x > upper.x && point.y < lower.y) { // lower right
//            return getEuclideanDistance(point, Vec3[upper.x, lower.y])
//
//        } else if (point.x < lower.x && point.y < lower.y) { // lower left
//            return getEuclideanDistance(point, Vec3[lower.x, lower.y])
//
//        } else {
//            return -1.0//throw Exception("That's not good")
//        }
//    }
}