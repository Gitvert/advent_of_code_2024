import kotlin.collections.forEach
import kotlin.text.split

fun day13 (lines: List<String>) {
    
    part1(lines)
    part2(lines)
}

fun part1(lines: List<String>) {
    val clawMachines = mutableListOf<ClawMachine>()

    for (i in lines.indices step 4) {
        val clawMachine = ClawMachine(
            ClawDistance(lines[i].split("X+")[1].split(",")[0].toLong(), lines[i].split("Y+")[1].toLong(), 3),
            ClawDistance(lines[i + 1].split("X+")[1].split(",")[0].toLong(), lines[i + 1].split("Y+")[1].toLong(), 1),
            ClawDistance(lines[i + 2].split("X=")[1].split(", ")[0].toLong(), lines[i + 2].split("Y=")[1].toLong(), 0)
        )

        clawMachines.add(clawMachine)
    }
    
    var sum = 0

    clawMachines.forEach{ clawMachine ->
        val playRounds = mutableSetOf<ClawDistance>()
        var cheapest = Int.MAX_VALUE
        playRounds.add(clawMachine.a)
        playRounds.add(clawMachine.b)
        for (i in 0 .. 200) {
            val newPlayRounds = mutableSetOf<ClawDistance>()
            playRounds.forEach { playRound ->
                if (playRound.x <= clawMachine.prize.x && playRound.y <= clawMachine.prize.y) {
                    newPlayRounds.add(ClawDistance(playRound.x + clawMachine.a.x, playRound.y + clawMachine.a.y, playRound.cost + clawMachine.a.cost))
                    newPlayRounds.add(ClawDistance(playRound.x + clawMachine.b.x, playRound.y + clawMachine.b.y, playRound.cost + clawMachine.b.cost))
                }
            }

            val cheapestCandidate = newPlayRounds
                .filter { it.x == clawMachine.prize.x && it.y == clawMachine.prize.y }
                .minByOrNull { it.cost }
                ?.cost

            if (cheapestCandidate != null && cheapestCandidate < cheapest) {
                cheapest = cheapestCandidate
            }

            playRounds.clear()
            playRounds.addAll(newPlayRounds)
        }


        if (cheapest < Int.MAX_VALUE) {
            sum += cheapest
        }
    }

    println("Day 13 part 1: $sum")
}

fun part2(lines: List<String>) {
    val offset = 10000000000000L
    val clawMachines = mutableListOf<ClawMachine>()

    for (i in lines.indices step 4) {
        val clawMachine = ClawMachine(
            ClawDistance(lines[i].split("X+")[1].split(",")[0].toLong(), lines[i].split("Y+")[1].toLong(), 3),
            ClawDistance(lines[i + 1].split("X+")[1].split(",")[0].toLong(), lines[i + 1].split("Y+")[1].toLong(), 1),
            ClawDistance(lines[i + 2].split("X=")[1].split(", ")[0].toLong() + offset, lines[i + 2].split("Y=")[1].toLong() + offset, 0)
        )

        clawMachines.add(clawMachine)
    }
    
    var sum = 0L

     clawMachines.forEach { clawMachine ->
         var equationSolve = solveUsingCramersRule(
            clawMachine.a.x.toDouble(),
            clawMachine.b.x.toDouble(),
            clawMachine.a.y.toDouble(),
            clawMachine.b.y.toDouble(),
            clawMachine.prize.x.toDouble(),
            clawMachine.prize.y.toDouble(),
        )
         
         if (equationSolve != null && equationSolve.first.isWholeNumber() && equationSolve.second.isWholeNumber()) {
             sum += (equationSolve.first.toLong() * 3 + equationSolve.second.toLong())
         }
    }

    println("Day 13 part 2: $sum")
}

fun solveUsingCramersRule(aX: Double, bX: Double, aY: Double, bY: Double, targetX: Double, targetY: Double): Pair<Double, Double>? {
    val d = aX * bY - bX * aY
    if (d == 0.0) {
        return null
    }
    val dX = targetX * bY - bX * targetY
    val dY = aX * targetY - targetX * aY
    val x = dX / d
    val b = dY / d
    
    return Pair(x, b)
}

fun Double.isWholeNumber(): Boolean {
    return this == this.toLong().toDouble()
}

data class ClawDistance(val x: Long, val y: Long, val cost: Int)

data class ClawMachine(val a: ClawDistance, val b: ClawDistance, val prize: ClawDistance)