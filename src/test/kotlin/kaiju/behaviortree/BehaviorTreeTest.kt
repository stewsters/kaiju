package kaiju.behaviortree

import org.junit.Test

val log = mutableListOf<String>()

class BehaviorTreeTest {
    @Test
    fun testBehaviorTree() {

        val situation = Situation(
            inRoom = false,
            opponentAlive = true
        )

        val root = Sequence(
            listOf(
                WalkIntoRoom(situation),
                Loop(
                    3,
                    Selector(
                        mapOf(
                            LeaveValue(situation) to WalkOutOfRoom(situation),
                            ShootValue(situation) to Sequence(
                                listOf(
                                    SayPunchline("Remember when I said I would kill you last?"),
                                    ShootYourShot(situation),
                                    SayPunchline("I lied.")
                                )
                            ),
                            NapValue(situation) to TakeANap(situation)
                        )
                    )
                )
            )
        )


        var i = 0
        do {
            val result = root.doIt()
            i++
        } while (result == running && i < 10)

        log.forEach { println(it) }

        assert(log[0] == "Walks into room")
        assert(log[1] == "Remember when I said I would kill you last?")
        assert(log[2] == "Shoot")
        assert(log[3] == "Walks out of room")
        assert(log[4] == "Take a nap")

    }
}

class TakeANap(val situation: Situation) : Task {
    override fun doIt(): Status {
        if (situation.opponentAlive)
            return failure

        log.add("Take a nap")
        return success
    }

}

class NapValue(val situation: Situation) : Condition {
    override fun valueToDoIt(): Float {
        return if (situation.opponentAlive || situation.inRoom) 0f else 1f
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
        log.add("Shoot")
        situation.opponentAlive = false
        return success
    }
}

class WalkIntoRoom(var situation: Situation) : Task {
    override fun doIt(): Status {
        log.add("Walks into room")
        situation.inRoom = true
        return success
    }

}

class LeaveValue(var situation: Situation) : Condition {
    override fun valueToDoIt(): Float {
        return if (situation.inRoom) 0.2f else 0f
    }
}

class WalkOutOfRoom(var situation: Situation) : Task {
    override fun doIt(): Status {
        log.add("Walks out of room")
        situation.inRoom = false
        return success
    }
}

class SayPunchline(val line: String) : Task {
    override fun doIt(): Status {
        log.add(line)
        return success
    }

}