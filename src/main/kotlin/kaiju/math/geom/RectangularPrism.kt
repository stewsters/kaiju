package kaiju.math.geom

import kaiju.math.Vec3

open class RectangularPrism(val lower: Vec3, val upper: Vec3) : Container3d {

    fun inside(point: Vec3): Boolean = inside(point.x, point.y, point.z)
    fun inside(x: Int, y: Int, z: Int): Boolean {
        return x >= lower.x && y >= lower.y && z >= lower.z
                && x <= upper.x && y <= upper.y && z <= upper.z
    }

    fun outside(point: Vec3): Boolean = outside(point.x, point.y, point.z)
    fun outside(x: Int, y: Int, z: Int): Boolean {
        return x < 0 || y < 0 || z < 0 || x > upper.x || y > upper.y || z > upper.z
    }

    fun getXSize() = upper.x - lower.x + 1
    fun getYSize() = upper.y - lower.y + 1
    fun getZSize() = upper.z - lower.z + 1

    override fun contains(x: Int, y: Int, z: Int) = inside(x, y, z)

    fun center(): Vec3 {
        val total = lower + upper
        return Vec3(total.x / 2, total.y / 2, total.z / 2)
    }

    fun volume(): Int {
        return (upper.x - lower.x + 1) * (upper.y - lower.y + 1) * (upper.z - lower.z + 1)
    }

    // Slice into floors based on z level
    fun slices(): List<RectangularPrism> {
        return (this.lower.z..this.upper.z).map {
            RectangularPrism(this.lower.copy(z = it), this.upper.copy(z = it))
        }
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
