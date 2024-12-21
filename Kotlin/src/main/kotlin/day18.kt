import java.util.PriorityQueue

val SIZE: Int = 71

fun day18 (lines: List<String>) {
    val corruptedBytes = lines.map { Position(it.split(',')[0].toInt(), it.split(',')[1].toInt()) }
    
    val shortestPath = findShortestPath(corruptedBytes.take(1024))
    
    println("Day 18 part 1: $shortestPath")

    for (i in 1025 until lines.size) {
        val shortestPath = findShortestPath(corruptedBytes.take(i))
        
        if (shortestPath == Int.MAX_VALUE) {
            println("Day 18 part 2: ${corruptedBytes[i - 1]}")
            break
        }
    }
}

fun findShortestPath(corruptedBytes: List<Position>): Int {
    val dx = arrayOf(-1, 0, 1, 0)
    val dy = arrayOf(0, 1, 0, -1)

    val distance = Array(SIZE) { IntArray(SIZE) { Int.MAX_VALUE } }

    distance[0][0] = 0

    val pq = PriorityQueue<GridPosition>(compareBy { it.cost })

    pq.add(GridPosition(0, 0, 0))

    while (pq.isNotEmpty()) {
        val current = pq.poll()

        for (i in 0..3) {
            val targetX = current.x + dx[i]
            val targetY = current.y + dy[i]
            val newCost = current.cost + 1

            if (isInGrid(targetX, targetY) && canMoveTo(Position(targetX, targetY), corruptedBytes)) {
                if (distance[targetY][targetX] > newCost) {

                    if (distance[targetY][targetX] != Int.MAX_VALUE) {
                        val adj = GridPosition(targetX, targetY, newCost)
                        pq.remove(adj)
                    }
                    
                    distance[targetY][targetX] = newCost
                    pq.add(GridPosition(targetX, targetY, newCost))
                }
            }
        }
    }

    return distance[SIZE - 1][SIZE - 1]
}

fun canMoveTo(pos: Position, corruptedBytes: List<Position>): Boolean {
    return !corruptedBytes.contains(pos)
}

fun isInGrid(x: Int, y: Int): Boolean {
    return x in 0 until SIZE && y in 0 until SIZE
}

data class GridPosition(val x: Int, val y: Int, val cost: Int)