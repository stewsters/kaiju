package kaiju.behaviortree

import org.junit.Test

class BehaviorTreeTest {
    @Test
    fun testBehaviorTree() {

        var situation = Situation(
                inRoom = false,
                opponentAlive = true
        )

        val inRoom = Selector(mapOf(
                LeaveValue(situation) to WalkOutOfRoom(situation),
                ShootValue(situation) to ShootYourShot(situation)
        ))

        val root = Sequence(listOf(
                WalkIntoRoom(situation),
                Loop(inRoom, 2)
        ))

        for (x in 0 until 100)
            root.doIt()

    }
}

class Situation(
        var inRoom: Boolean = false,
        var opponentAlive: Boolean = true
)

class ShootValue(var situation: Situation) : Condition {
    override fun valueToDoIt(): Float {
        return if (situation.opponentAlive) 1f else 0f
    }

}

class ShootYourShot(var situation: Situation) : Task {
    override fun doIt(): Status {
        println("Shoot")
        situation.opponentAlive = false
        return Status.Success
    }
}

class WalkIntoRoom(var situation: Situation) : Task {
    override fun doIt(): Status {
        println("Walks into room")
        situation.inRoom = true
        return Status.Success
    }

}

class LeaveValue(var situation: Situation) : Condition {
    override fun valueToDoIt(): Float {
        return if (situation.inRoom) 0.2f else 0f
    }
}

class WalkOutOfRoom(var situation: Situation) : Task {
    override fun doIt(): Status {
        println("Walks out of room")
        situation.inRoom = false
        return Status.Success
    }
}