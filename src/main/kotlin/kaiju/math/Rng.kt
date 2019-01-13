package kaiju.math

import com.github.alexeyr.pcg.Pcg32

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

fun getInt(intRange: IntRange, rng: Pcg32 = defaultRng): Int = getIntInRange(intRange.first, intRange.endInclusive, rng)


fun d(high: Int, rng: Pcg32 = defaultRng): Int = getIntInRange(1, high, rng)

// this is done so I can call it in the integer
fun dice(high: Int, rng: Pcg32 = defaultRng): Int = d(high, rng)

fun Int.d(high: Int, rng: Pcg32 = defaultRng): Int = (0 until this).map { dice(high, rng) }.sum()


fun getFloatInRange(low: Float, high: Float, rng: Pcg32 = defaultRng): Float {
    if (low == high) return low
    return rng.nextFloat() * (high - low) + low
}

fun getDoubleInRange(low: Double, high: Double, rng: Pcg32 = defaultRng): Double {
    if (low == high) return low
    return rng.nextFloat() * (high - low) + low
}

fun getBoolean(rng: Pcg32 = defaultRng): Boolean = rng.nextBoolean()

fun getBoolean(chance: Float, rng: Pcg32 = defaultRng): Boolean = rng.nextFloat() <= chance

fun getBoolean(chance: Double, rng: Pcg32 = defaultRng): Boolean = rng.nextDouble() <= chance

fun getGauss(stdDeviation: Double, rng: Pcg32 = defaultRng): Double {
    return rng.nextGaussian() * stdDeviation
}

// .random() can do this, but we may need a different Random function
// fun <E> rand(source: List<E>, rng: Pcg32 = defaultRng): E = source[getIntInRange(0, source.size - 1, rng)]
//
// fun <T> rand(source: Array<T>, rng: Pcg32 = defaultRng): T = source[getIntInRange(0, source.size - 1, rng)]
