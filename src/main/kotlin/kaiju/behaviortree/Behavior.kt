package kaiju.behaviortree

enum class Status {
    SUCCESS,
    FAILURE,
    RUNNING
}


// Does something in the game
interface Task {
    fun doIt(): Status // returns if we are done doing this action
}


// Composites

// Executes every node in order
class Sequence(val tasks: List<Task>, var i: Int = 0) : Task {
    override fun doIt(): Status {
        val current = tasks[i]

        if (current.doIt()) {
            // Sub action completed
            i++
            if (i >= tasks.size) {
                i = 0
                return true
            }
        }
        // Still going
        return Status.RUNNING
    }
}

// Decorators
class Selector(val tasks: Map<Condition, Task>) : Task {
    override fun doIt(): Status {
        val current = tasks.maxBy { it.key.valueToDoIt() }?.value

        // if there is no option, we are done
        if (current == null) {
            return Status.FAILURE
        }

        return current.doIt()
    }

}

interface Condition {
    fun valueToDoIt(): Float
}


// inverter
// succeeder  - always return success

// Repeater -
class Loop(val task: Task, val count: Int) : Task {
    override fun doIt(): Status {
        task.doIt()
    }
}

class LoopUntilSuccess(val task: Task) // keeps going until it reutns success
class LoopUntilFailure(val task: Task) // keeps

class Parallel()  // failuremode any/all, successMode any/all

// wait?