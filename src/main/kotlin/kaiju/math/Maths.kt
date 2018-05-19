package kaiju.math

import com.github.alexeyr.pcg.Pcg32
import java.util.ArrayList


// Math

fun manhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2)
}

// Manhattan distance with diagonals
fun chebyshevDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
    return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2))
}



fun min(a: Int, b: Int): Int {
    return if (a < b) a else b
}

fun min(a: Int, b: Int, c: Int): Int {
    return if (min(a, b) < c) min(a, b) else c
}

fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}

fun pow(a: Int, b: Int): Int {
    return if (b > 1)
        a * pow(a, b - 1)
    else
        a
}


fun limit(number: Int, low: Int, high: Int): Int {
    return Math.max(low, Math.min(high, number))
}

fun limit(number: Float, low: Float, high: Float): Float {
    return Math.max(low, Math.min(high, number))
}

fun limit(number: Double, low: Double, high: Double): Double {
    return Math.max(low, Math.min(high, number))
}

fun <T : Comparable<T>> T.limit(low: T, high: T): T =
        if (this < low)
            low
        else if (this > high)
            high
        else this




// Linear Interpolate
fun lerp(percentage: Double, one: Double, two: Double): Double =
        one + (two - one) * percentage

fun lerp(percentage: Float, min: Float, max: Float): Float =
     min + (max - min) * percentage


fun unlerp(min: Float, max: Float, value: Float): Float {
    return (value - min) / (max - min)
}

fun unlerp(min: Double, max: Double, value: Double): Double {
    return (value - min) / (max - min)
}


fun smootherStep(edge0: Float, edge1: Float, x: Float): Float {
    var x = x
    // Scale, and clamp x to 0..1 range
    x = Math.max(0f, Math.min(1f, (x - edge0) / (edge1 - edge0)))
    // Evaluate polynomial
    return x * x * x * (x * (x * 6 - 15) + 10)
}


fun getChoice(choicesMap: Map<String, Int>, rng:Pcg32 = defaultRng): String? {
    var totalChances = 0
    for (value in choicesMap.values) {
        totalChances += value
    }
    val dice = getIntInRange(0, totalChances, rng)
    var runningTotal = 0
    for ((key, value) in choicesMap) {
        runningTotal += value
        if (dice <= runningTotal)
            return key
    }
    return null
}

/**
 * Faster version of Math.floor
 *
 * @param x A double
 * @return The int value of the floored double
 */
fun fastFloor(x: Double): Int {
    return if (x > 0) x.toInt() else x.toInt() - 1
}

/**
 * Faster version of Math.floor
 *
 * @param x A float
 * @return The int value of the floored float
 */
fun fastFloor(x: Float): Int {
    return if (x > 0) x.toInt() else x.toInt() - 1
}
