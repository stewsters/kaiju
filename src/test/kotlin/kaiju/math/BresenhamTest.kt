package kaiju.math

import org.junit.Test

class BresenhamTest {

    @Test
    fun testBresenham() {
        val start = Vec2(0, 0)
        val end = Vec2(2, 2)

        // Block at 1,1
        assert(!los(start, end) { x, y -> x != 1 && y != 1 })
        // all clear
        assert(los(start, end) { x, y -> true })

    }
}