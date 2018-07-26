package kaiju.math

import com.github.alexeyr.pcg.Pcg32
import java.util.*

// Random Math functions
val defaultRng = Pcg32()

// Random
/**
 * @param low  inclusive
 * @param high inclusive
 * @return The integer
 */
fun getIntInRange(low: Int, high: Int, rng: Pcg32 = defaultRng): Int {
    if (low == high) return low
    return rng.nextInt(high + 1 - low) + low
}

fun d(high: Int, rng: Pcg32 = defaultRng): Int {
    return getIntInRange(1, high, rng)
}

fun getFloatInRange(low: Float, high: Float, rng: Pcg32 = defaultRng): Float {
    if (low == high) return low
    return rng.nextFloat() * (high - low) + low
}

fun getBoolean(rng: Pcg32 = defaultRng): Boolean {
    return rng.nextBoolean()
}

fun getBoolean(chance: Float, rng: Pcg32 = defaultRng): Boolean {
    return rng.nextFloat() <= chance
}

fun getGauss(stdDeviation: Double, rng: Pcg32 = defaultRng): Double {
    return rng.nextGaussian() * stdDeviation
}

fun <E> rand(source: ArrayList<E>, rng: Pcg32 = defaultRng): E {
    val id = getIntInRange(0, source.size - 1, rng)
    return source[id]
}

fun <T> randVal(source: Array<T>, rng: Pcg32 = defaultRng): T {
    val id = getIntInRange(0, source.size - 1, rng)
    return source[id]
}
