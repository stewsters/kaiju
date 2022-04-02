package kaiju.geom

import kaiju.math.Vec3
import kaiju.math.geom.RectangularPrism
import org.junit.Test

class RectangularPrismTest {
    @Test
    fun testVolume() {
        val small = RectangularPrism(Vec3(0, 0, 0), Vec3(0, 0, 0))
        assert(small.volume() == 1)
    }
}