package kaiju.mapgen.two.predicate


import kaiju.mapgen.noise.NoiseFunction2d
import kaiju.math.Matrix2d


fun <T> noiseGreaterThan(noiseFunction2d: NoiseFunction2d, threshold: Double) = { map: Matrix2d<T>, x: Int, y: Int ->
    noiseFunction2d.gen(x.toDouble(), y.toDouble()) > threshold
}
