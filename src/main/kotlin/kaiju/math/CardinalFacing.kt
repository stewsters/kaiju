package kaiju.math

import com.github.alexeyr.pcg.Pcg32

enum class CardinalFacing(val offset: Vec2){
    NORTH(Vec2(0, 1)),
    EAST(Vec2(1, 0)),
    SOUTH(Vec2(0, -1)),
    WEST(Vec2(-1, 0));

    fun left(): Facing = Facing.values()[(this.ordinal + 1) % Facing.values().size]
    fun right(): Facing = Facing.values()[(this.ordinal - 1 + Facing.values().size) % Facing.values().size]

    fun reverse(): Facing {
        return Facing.values()[(ordinal + 2) % Facing.values().size]
    }
    companion object {
        fun random(pcg: Pcg32) = when (pcg.nextInt(4)) {
            0 -> NORTH
            1 -> EAST
            2 -> SOUTH
            else -> WEST
        }
    }
}