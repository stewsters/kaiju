package kaiju.mapgen.noise

import kaiju.noise.OpenSimplexNoise

class NoiseFunction3d(
        val xOffset: Double,
        val yOffset: Double,
        val zOffset: Double,
        val xScale: Double,
        val yScale: Double,
        val zScale: Double,
        val openSimplexNoise: OpenSimplexNoise = OpenSimplexNoise()
) {

    /**
     * Returns a value between 0 and 1
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return The generated value scaled
     */
    fun gen(x: Double, y: Double, z: Double): Double {
        return openSimplexNoise.random3D(x / xScale + xOffset, y / yScale + yOffset, z / zScale + zOffset) + 0.5f
    }


}