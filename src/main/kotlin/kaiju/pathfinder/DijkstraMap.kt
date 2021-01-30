package kaiju.pathfinder

import kaiju.math.*
import java.util.*

// http://www.roguebasin.com/index.php?title=The_Incredible_Power_of_Dijkstra_Maps

fun dijkstraMap2d(size: Vec2, goals: List<Vec2>, blocked: (p: Vec2) -> Boolean): Matrix2d<Int> {
    // clear out map
    val map = matrix2dOf(size.x, size.y) { x, y -> Int.MAX_VALUE }
    val frontier = PriorityQueue<Vec2>(goals.size) { x, y -> map[x].compareTo(map[y]) }

    // put all the points in the frontier
    for (goal in goals) {
        map[goal] = 0
        frontier.add(goal)
    }


    while (frontier.isNotEmpty()) {
        val evaluating = frontier.poll()!!

        val newVal = map[evaluating] + 1
        evaluating.vonNeumanNeighborhood().forEach {
            if (map.contains(it) && map[it] > newVal && !blocked(it)) {
                map[it] = newVal
                frontier.add(it)
            }
        }

    }

    return map

}


fun dijkstraMap3d(size: Vec3, goals: List<Vec3>, blocked: (p: Vec3) -> Boolean): Matrix3d<Int> {
    // clear out map
    val map = matrix3dOf(size) { x, y, z -> Int.MAX_VALUE }
    val frontier = PriorityQueue<Vec3>(goals.size) { x, y -> map[x].compareTo(map[y]) }

    // put all the points in the frontier
    for (goal in goals) {
        map[goal] = 0
        frontier.add(goal)
    }


    while (frontier.isNotEmpty()) {
        val evaluating = frontier.poll()!!

        val newVal = map[evaluating] + 1
        evaluating.vonNeumanNeighborhood().forEach {
            if (map.contains(it) && map[it] > newVal && !blocked(it)) {
                map[it] = newVal
                frontier.add(it)
            }
        }

    }

    return map

}