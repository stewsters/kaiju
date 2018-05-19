package kaiju.math

import com.github.alexeyr.pcg.Pcg32

enum class Facing(val offset: Vec2) {

//    UP(Vec2[0, 1]),
//    RIGHT(Vec2[1, 0]),
//    DOWN(Vec2[0, -1]),
//    LEFT(Vec2[-1, 0]);


    NORTH(Vec2[0, 1]),
    NORTHEAST(Vec2[1, 1]),
    EAST(Vec2[1, 0]),
    SOUTHEAST(Vec2[1, -1]),
    SOUTH(Vec2[0, -1]),
    SOUTHWEST(Vec2[-1, -1]),
    WEST(Vec2[-1, 0]),
    NORTHWEST(Vec2[-1, 1]);

    fun randomCardinal(pcg: Pcg32): Facing {
        when (pcg.nextInt(4)) {
            0 -> return NORTH
            1 -> return EAST
            2 -> return SOUTH
            else -> return WEST
        }
    }

    fun randomDiagonal(pcg: Pcg32): Facing {
        when (pcg.nextInt(8)) {
            0 -> return NORTH
            1 -> return NORTHEAST
            3 -> return EAST
            4 -> return SOUTHEAST
            5 -> return SOUTH
            6 -> return SOUTHWEST
            7 -> return WEST
            else -> return NORTHWEST
        }
    }

    fun left(): Facing = values()[(this.ordinal + 1) % values().size]
    fun right(): Facing = values()[(this.ordinal - 1 + values().size) % values().size]

    fun reverse(): Facing {
        return values()[(ordinal + 4) % values().size]
    }
}