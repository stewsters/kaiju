package kaiju.pathfinder

import kaiju.datastructure.PriorityQueue
import kaiju.math.Vec2
import kaiju.math.matrix2dOf


/**
 * AStar 2d
 */
fun findPath2d(
    size: Vec2,
    cost: (Vec2) -> Double,
    heuristic: (Vec2, Vec2) -> Double,
    neighbors: (Vec2) -> List<Vec2>,
    start: Vec2,
    end: Vec2
): List<Vec2>? {

    val costs = matrix2dOf(size.x, size.y) { _, _ -> Double.MAX_VALUE }
    val parent = matrix2dOf<Vec2?>(size.x, size.y) { _, _ -> null }
    val fScore = matrix2dOf(size.x, size.y) { _, _ -> Double.MAX_VALUE }

    val openSet = PriorityQueue<Vec2> { one, two -> fScore[one].compareTo(fScore[two]) }
    val closeSet = HashSet<Vec2>()

    openSet.add(start)
    costs[start] = 0.0
    fScore[start] = heuristic(start, end)

    while (openSet.isNotEmpty()) {

        // Grab the next node with the lowest cost
        val cheapestNode: Vec2 = openSet.poll() ?: return null

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

        closeSet.add(cheapestNode)

        // get the neighbors
        //  for each point, set the cost, and a pointer back if we set the cost
        for (it in neighbors(cheapestNode)) {
            if (it.x < 0 || it.y < 0 || it.x >= size.x || it.y >= size.y)
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
