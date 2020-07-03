package kaiju.mapgen.three.predicate


import kaiju.mapgen.noise.NoiseFunction3d
import kaiju.math.Matrix3d


fun <T> noiseGreaterThan(noiseFunction3d: NoiseFunction3d, threshold: Double) = { map: Matrix3d<T>, x: Int, y: Int, z: Int ->
    noiseFunction3d.gen(x.toDouble(), y.toDouble(), z.toDouble()) > threshold
}
