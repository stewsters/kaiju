package kaiju.pathfinder

import kaiju.math.Matrix3d
import kaiju.math.Vec3

/**
 * AStar 3d
 */
fun findPath3d(
        size: Vec3,
        cost: (Vec3) -> Double,
        heuristic: (Vec3, Vec3) -> Double,
        neighbors: (Vec3) -> List<Vec3>,
        start: Vec3,
        end: Vec3
): List<Vec3>? {

    val costs = Matrix3d(size.x, size.y, size.z) { _, _, _ -> Double.MAX_VALUE }
    val parent = Matrix3d<Vec3?>(size.x, size.y, size.z) { _, _, _ -> null }
    val fScore = Matrix3d(size.x, size.y, size.z) { _, _, _ -> Double.MAX_VALUE }

    val openSet = mutableListOf<Vec3>()
    val closeSet = HashSet<Vec3>()

    openSet.add(start)
    costs[start] = 0.0
    fScore[start] = heuristic(start, end)

    while (openSet.isNotEmpty()) {

        // Grab the next node with the lowest cost
        val cheapestNode: Vec3 = openSet.minBy { fScore[it] }!!

        if (cheapestNode == end) {
            // target found, we have a path
            val path = mutableListOf(cheapestNode)

            var node = cheapestNode
            while (parent[node] != null) {
                node = parent[node]!!
                path.add(node)
            }
            return path.reversed()
        }

        openSet.remove(cheapestNode)
        closeSet.add(cheapestNode)

        // get the neighbors
        // for each point, set the cost, and a pointer back if we set the cost
        for (it in neighbors(cheapestNode)) {
            if (it.x < 0 || it.y < 0 || it.z < 0 || it.x >= size.x || it.y >= size.y || it.z >= size.z)
                continue

            if (closeSet.contains(it))
                continue

            val nextCost = costs[cheapestNode] + cost(it)

            if (nextCost < costs[it]) {
                costs[it] = nextCost
                fScore[it] = nextCost + heuristic(it, end)
                parent[it] = cheapestNode


                if (closeSet.contains(it)) {
                    closeSet.remove(it)
                }
                openSet.add(it)
            }
        }
    }

    // could not find a path
    return null

}