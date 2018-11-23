package kaiju.behaviortree

sealed class Status {
    object Running : Status()
    object Success : Status()
    object Failure : Status()
}
typealias running = Status.Running // not finished
typealias success = Status.Success // succeeded
typealias failure = Status.Failure // anything else


// Does something in the game
interface Task {
    fun doIt(): Status // returns if we are done doing this action
}

// This is the root of the tree
class BehaviorTree(val root: Task, current: Task = root) {
    // This needs to store a tree of tasks and your current position in it.
    fun doIt(): Status {

    }


}

// Control flow nodes

/**
 * Executes every node in order until one failed
 */
class Sequence(vararg val tasks: Task, var i: Int = 0) : Task {
    override fun doIt(): Status =
            when (tasks[i].doIt()) {
                Status.Success -> {
                    i++
                    if (i >= tasks.size) {
                        i = 0
                        success
                    } else
                        running
                }
                Status.Failure -> {
                    i = 0
                    failure
                }
                Status.Running -> running
            }
}

/**
 * find and execute first child that does not fail (fallback composition)
 */
class Selector(val tasks: List<Task>, var i: Int = 0) : Task {
    override fun doIt(): Status =
            when (tasks[i].doIt()) {
                Status.Success -> {
                    i = 0
                    success
                }
                Status.Failure -> {
                    i++
                    if (i >= tasks.size) {
                        i = 0
                        failure
                    } else
                        running
                }
                Status.Running -> running
            }
}

// Execute one action with the highest score
class Chooser(val tasks: Map<Condition, Task>, var current: Task? = null) : Task {


    override fun doIt(): Status {

        if (current == null) {
            val current = tasks.maxBy { it.key.valueToDoIt() }?.value

            // if there is no option, we are done
            if (current == null) {
                return failure
            }

            return current.doIt()

        } else {
            // if we are running, keep it up
            // else clear the current and return
            val result = current!!.doIt()
            if (result != running) {
                current = null
            }
            return result

        }

    }

}

interface Condition {
    fun valueToDoIt(): Float
}

// inverter
// succeeder  - always return success

// Repeater - keep doing it x number of times
class Loop(val iterations: Int, val task: Task) : Task {
    private var count: Int = 0

    override fun doIt(): Status {

        val result = task.doIt()

        if (result != running) {
            count++
            if (count >= iterations)
                return success
        }

        return running
    }
}

class LoopForever(val task: Task) : Task {

    override fun doIt(): Status {
        task.doIt()
        return running
    }
}


class LoopUntilSuccess(val task: Task) : Task {
    override fun doIt(): Status {
        val result = task.doIt()
        if (result == success)
            return success

        return running
    }
}

class LoopUntilFailure(val task: Task) : Task {
    override fun doIt(): Status {
        val result = task.doIt()
        if (result == failure)
            return success

        return running
    }
}

//class Parallel()  // failuremode any/all, successMode any/all

// wait?