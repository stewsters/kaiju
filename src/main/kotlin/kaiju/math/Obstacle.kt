package kaiju.math

interface Obstacle {
    fun minDist(point: Vec2): Double
}