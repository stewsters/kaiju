package kaiju.math

import java.lang.Math.abs

open class Rectangle(val lower: Vec2, val upper: Vec2) : Obstacle, Container2d {

    fun inside(point: Vec2): Boolean = inside(point.x, point.y)
    fun inside(x: Int, y: Int): Boolean {
        return x >= lower.x && y >= lower.y && x <= upper.x && y <= upper.y
    }

    fun outside(point: Vec2) = outside(point.x, point.y)
    fun outside(x: Int, y: Int): Boolean {
        return x < 0 || y < 0 || x > upper.x || y > upper.y
    }

    fun getXSize(): Int = upper.x - lower.x
    fun getYSize(): Int = upper.y - lower.y

    fun center(): Vec2 {
        val total = lower + upper
        return Vec2[total.x / 2, total.y / 2]
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

    override fun contains(x: Int, y: Int): Boolean = inside(x, y)

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

    fun inside(point: Vec3): Boolean = inside(point.x, point.y, point.z)
    fun inside(x: Int, y: Int, z: Int): Boolean {
        return x >= lower.x && y >= lower.y && y >= lower.z
                && x <= upper.x && y <= upper.y && z <= upper.z
    }

    fun outside(point: Vec3): Boolean = outside(point.x, point.y, point.z)
    fun outside(x: Int, y: Int, z: Int): Boolean {
        return x < 0 || y < 0 || z < 0 || x > upper.x || y > upper.y || z > upper.z
    }

    fun getXSize() = upper.x - lower.x
    fun getYSize() = upper.y - lower.y
    fun getZSize() = upper.z - lower.z

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