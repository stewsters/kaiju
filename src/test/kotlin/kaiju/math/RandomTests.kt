package kaiju.math

import com.github.alexeyr.pcg.Pcg32
import org.junit.Test
import kotlin.math.max


class RandomTests {

    @Test
    fun pcg32() {
        // We use pcg32 for randomness here
        val ran = Pcg32()

        ran.nextInt(10)

    }

    @Test
    fun intInRange() {

        // generate an integer in a range
        val num = getIntInRange(0, 10)
        assert(num >= 0)
        assert(num <= 10)

        // inclusive
        val num2 = getInt(0 until 10)
        assert(num2 >= 0)
        assert(num2 < 10)

        // not inclusive
        val num3 = getInt(0..10)
        assert(num3 >= 0)
        assert(num3 <= 10)

    }

    @Test
    fun floatInRange() {

        val num = getFloatInRange(0f, 10f)
        assert(num >= 0)
        assert(num <= 10)

    }

    @Test
    fun testBoolean() {
        getBoolean()

        getBoolean(0.2f)
    }

    @Test
    fun dice() {

        // You can roll a dice with d(20)
        println(d(20))


        // You can do some common roll types
        println("3d6:" + 3.d(6))

        // you can combine dice rolls into common rolls.
        val twoDSixWithAdvantage = { max(d(6), d(6)) }

        println(twoDSixWithAdvantage())


    }


}

