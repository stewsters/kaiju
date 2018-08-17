package kaiju.behaviortree

sealed class Status {
    object Success : Status()
    object Failure : Status()
    object Running : Status()
}
typealias success = Status.Success
typealias failure = Status.Failure
typealias running = Status.Running


// Does something in the game
interface Task {
    fun doIt(): Status // returns if we are done doing this action
}


// Composites

// Executes every node in order
class Sequence(val tasks: List<Task>, var i: Int = 0) : Task {
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
                Status.Failure -> failure
                Status.Running -> running
            }
}

// Decorators
class Selector(val tasks: Map<Condition, Task>) : Task {
    override fun doIt(): Status {
        val current = tasks.maxBy { it.key.valueToDoIt() }?.value

        // if there is no option, we are done
        if (current == null) {
            return failure
        }

        return current.doIt()
    }

}

interface Condition {
    fun valueToDoIt(): Float
}


// inverter
// succeeder  - always return success

// Repeater - keep doing it x number of times
class Loop(val task: Task, val iterations: Int) : Task {
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