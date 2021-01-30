package kaiju.mapgen.noise

import kaiju.noise.OpenSimplexNoise

class NoiseFunction2d(
    val xOffset: Double,
    val yOffset: Double,
    val xScale: Double,
    val yScale: Double,
    val openSimplexNoise: OpenSimplexNoise = OpenSimplexNoise()
) {

    /**
     * Returns a value between 0 and 1
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The generated value
     */
    fun gen(x: Double, y: Double): Double {
        return openSimplexNoise.random2D(x / xScale + xOffset, y / yScale + yOffset) + 0.5
    }


}