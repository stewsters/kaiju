package kaiju.mapgen.three.predicate

import kaiju.math.Matrix3d
import kotlin.random.Random


fun <T> random(percentage: Double) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    Random.nextDouble() < percentage
}