fun day13 (lines: List<String>) {
    val clawMachines = mutableListOf<ClawMachine>()
    
    for (i in lines.indices step 4) {
        val clawMachine = ClawMachine(
            ClawDistance(lines[i].split("X+")[1].split(",")[0].toInt(), lines[i].split("Y+")[1].toInt(), 3),
            ClawDistance(lines[i + 1].split("X+")[1].split(",")[0].toInt(), lines[i + 1].split("Y+")[1].toInt(), 1),
            ClawDistance(lines[i + 2].split("X=")[1].split(", ")[0].toInt(), lines[i + 2].split("Y=")[1].toInt(), 0)
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

data class ClawDistance(val x: Int, val y: Int, val cost: Int)

data class ClawMachine(val a: ClawDistance, val b: ClawDistance, val prize: ClawDistance)