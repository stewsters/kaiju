package kaiju.mapgen.noise

import kaiju.noise.OpenSimplexNoise

class NoiseFunction2d(
    private val xOffset: Double,
    private val yOffset: Double,
    private val xScale: Double,
    private val yScale: Double,
    private val openSimplexNoise: OpenSimplexNoise = OpenSimplexNoise()
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