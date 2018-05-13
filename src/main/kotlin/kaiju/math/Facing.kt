package kaiju.math

enum class Facing(val offset: Vec2) {

    UP(Vec2[0, 1]),
    RIGHT(Vec2[1, 0]),
    DOWN(Vec2[0, -1]),
    LEFT(Vec2[-1, 0]);

    fun left(): Facing = values()[(this.ordinal + 1) % values().size]
    fun right(): Facing = values()[(this.ordinal - 1 + values().size) % values().size]

}