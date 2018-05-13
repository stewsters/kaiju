package kaiju.math

// Random Math functions

// Linear Interpolate
fun lerp(percentage: Double, one: Double, two: Double): Double =
        one + (two - one) * percentage


fun <T : Comparable<T>> T.limit(low: T, high: T): T =
        if (this < low)
            low
        else if (this > high)
            high
        else this
