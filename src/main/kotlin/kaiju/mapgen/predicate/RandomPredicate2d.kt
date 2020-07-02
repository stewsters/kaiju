package mapgen.predicate

import kaiju.math.Matrix2d
import kotlin.random.Random


fun <T> random(percentage: Double) = { map: Matrix2d<T>, x: Int, y: Int ->
    Random.nextDouble() < percentage
}