package kaiju.noise

fun fbm(el: OpenSimplexNoise, x: Double, y: Double, octaves: Int, frequency: Double, amplitude: Double, lacunarity: Double, gain: Double): Double {
    var freq = frequency
    var amp = amplitude
    var total = 0.0
    for (i in 0 until octaves) {
        total += el.random2D(x * freq, y * freq) * amp
        freq *= lacunarity
        amp *= gain
    }
    return total
}