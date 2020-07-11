package kaiju.geom

import kaiju.math.Matrix2d
import org.junit.Test


class Matrix {

    @Test
    fun testPrint() {

        val matrix = Matrix2d(3, 3) { x, y -> x * y }

        matrix.forEachIndexed { x, y, value ->
            if (x == matrix.getSize().x - 1)
                println("$value ")
            else
                print("$value ")
        }

    }


}
