package kaiju.pathfinder

import java.util.*

/**
 * AStar
 */
fun <T> findGenericPath(
    cost: (T, T) -> Double,
    heuristic: (T, T) -> Double,
    neighbors: (T) -> List<T>,
    start: T,
    end: T
): List<T>? {

    val costs = mutableMapOf<T, Double>()
    val parent = mutableMapOf<T, T>()
    val fScore = mutableMapOf<T, Double>()

    val openSet = PriorityQueue<T> { one, two ->
        (fScore[one] ?: Double.MAX_VALUE).compareTo(fScore[two] ?: Double.MAX_VALUE)
    }
    val closeSet = HashSet<T>()

    openSet.add(start)
    costs[start] = 0.0
    fScore[start] = heuristic(start, end)

    while (openSet.isNotEmpty()) {

        // Grab the next node with the lowest cost
        val cheapestNode = openSet.poll()

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

            if (closeSet.contains(it))
                continue

            val nextCost = (costs[cheapestNode] ?: Double.MAX_VALUE )+ cost(cheapestNode, it)

            if (nextCost < (costs[it] ?: Double.MAX_VALUE)) {
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
