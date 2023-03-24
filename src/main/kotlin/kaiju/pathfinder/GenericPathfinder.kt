package kaiju.pathfinder

import java.util.*

sealed interface Path<T> {
    data class Success<T>(val data: List<T>) : Path<T>
    class NotFound<T> : Path<T> // exhausted all possible search options
    class MaxDistanceExceeded<T> : Path<T> // we went too far
}

/**
 * AStar
 */
fun <T> findGenericPath(
    cost: (T, T) -> Double,
    heuristic: (T, T) -> Double,
    neighbors: (T) -> List<T>,
    start: T,
    end: T,
    maxCost: Double? = null
): Path<T> {

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

        if (maxCost != null && ((costs[cheapestNode] ?: Double.MAX_VALUE) > maxCost)) {
            return Path.MaxDistanceExceeded()
        } else if (cheapestNode == end) {
            // target found, we have a path
            val path = mutableListOf(cheapestNode)

            var node = cheapestNode
            while (parent[node] != null) {
                node = parent[node]!!
                path.add(node)
            }
            return Path.Success(path.reversed())
        }

        closeSet.add(cheapestNode)

        // get the neighbors
        //  for each point, set the cost, and a pointer back if we set the cost
        for (it in neighbors(cheapestNode)) {

            if (closeSet.contains(it))
                continue

            val nextCost = (costs[cheapestNode] ?: Double.MAX_VALUE) + cost(cheapestNode, it)

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

    // could not find a path, open set empty
    return Path.NotFound()

}
