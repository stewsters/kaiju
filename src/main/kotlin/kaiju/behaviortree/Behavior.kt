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
    override fun doIt(): Status =
            when (tasks[i].doIt()) {
                Status.SUCCESS -> {
                    i++
                    if (i >= tasks.size) {
                        i = 0
                        Status.SUCCESS
                    } else
                        Status.RUNNING
                }
                Status.FAILURE -> Status.FAILURE
                Status.RUNNING -> Status.RUNNING
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

// Repeater - keep doing it x number of times
class Loop(val task: Task, val iterations: Int) : Task {
    private var count: Int = 0

    override fun doIt(): Status {

        val result = task.doIt()

        if (result != Status.RUNNING) {
            count++
            if (count >= iterations)
                return Status.SUCCESS
        }

        return Status.RUNNING
    }
}

class LoopForever(val task: Task) : Task {

    override fun doIt(): Status {
        task.doIt()
        return Status.RUNNING
    }
}


class LoopUntilSuccess(val task: Task) : Task {
    override fun doIt(): Status {
        val result = task.doIt()
        if (result == Status.SUCCESS)
            return Status.SUCCESS

        return Status.RUNNING
    }
}

class LoopUntilFailure(val task: Task) : Task {
    override fun doIt(): Status {
        val result = task.doIt()
        if (result == Status.FAILURE)
            return Status.SUCCESS

        return Status.RUNNING
    }
}

//class Parallel()  // failuremode any/all, successMode any/all

// wait?