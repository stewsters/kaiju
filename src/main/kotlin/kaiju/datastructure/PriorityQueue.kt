package kaiju.datastructure

class PriorityQueue<T>(
    size: Int = 10,
    val comparator: Comparator<T>
) {

    private val items = ArrayList<T>(size)

    fun isEmpty(): Boolean = items.isEmpty()

    //    fun size(): Int = items.size
    val size get() = items.size

    fun clear() {
        items.clear()
    }

    fun peek(): T? = items.firstOrNull()

    fun add(element: T) {
        items.add(element)
        siftUp(items.size - 1)
    }

    fun remove(element: T) {
        items.remove(element)
    }

    fun poll(): T? {
        if (isEmpty()) return null

        if (size == 1) {
            return items.removeAt(0)
        }

        val minItem = items[0]
        items[0] = items.removeAt(items.size - 1)
        siftDown(0)
        return minItem
    }

    private fun siftUp(index: Int) {
        var childIndex = index
        while (childIndex > 0) {
            val parentIndex = (childIndex - 1) / 2
            if (comparator.compare(items[childIndex],  items[parentIndex])>=0) break
            swap(childIndex, parentIndex)
            childIndex = parentIndex
        }
    }

    private fun siftDown(index: Int) {
        var parentIndex = index
        while (true) {
            var minChildIndex = parentIndex
            val leftChildIndex = 2 * parentIndex + 1
            val rightChildIndex = leftChildIndex + 1

            if (leftChildIndex < items.size && comparator.compare(items[leftChildIndex],  items[minChildIndex])<0) {
                minChildIndex = leftChildIndex
            }
            if (rightChildIndex < items.size && comparator.compare(items[rightChildIndex] , items[minChildIndex])<0) {
                minChildIndex = rightChildIndex
            }

            if (minChildIndex == parentIndex) break
            swap(parentIndex, minChildIndex)
            parentIndex = minChildIndex
        }
    }

    private fun swap(i: Int, j: Int) {
        val temp = items[i]
        items[i] = items[j]
        items[j] = temp
    }

    override fun toString(): String {
        return items.toString()
    }

    fun isNotEmpty(): Boolean {
        return items.isNotEmpty()
    }


}