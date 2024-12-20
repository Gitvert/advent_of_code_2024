import java.util.PriorityQueue

fun day16 (lines: List<String>) {
    val start = findStartCoordinate(lines)!!
    
    val mazeMap = parseMazeMap(lines)
    val reindeer = Reindeer(start, 0, Direction.RIGHT, mutableListOf())
    
    val answer = findShortestPath(mazeMap, reindeer)
    
    println("Day 16 part 1: ${answer.first}")
    println("Day 16 part 2: ${answer.second.size}")
}

fun findShortestPath(mazeMap: List<List<Char>>, reindeer: Reindeer): Pair<Int, Set<Position>> {
    val dx = arrayOf(-1, 0, 1, 0)
    val dy = arrayOf(0, 1, 0, -1)

    val distance = mutableMapOf<Pair<Position, Direction>, Int>().withDefault { Int.MAX_VALUE }
    var lowest = Int.MAX_VALUE
    val paths = mutableSetOf(reindeer.pos)
    
    distance[Pair(reindeer.pos, Direction.RIGHT)] = 0

    val pq = PriorityQueue<Reindeer>(compareBy { it.cost })

    pq.add(reindeer)

    while (pq.isNotEmpty()) {
        val current = pq.poll()

        for (i in 0..3) {
            val targetX = current.pos.x + dx[i]
            val targetY = current.pos.y + dy[i]
            
            val newReindeerPosition = costToMoveTo(Position(targetX, targetY), mazeMap, current)
            if (newReindeerPosition != null) {
                if (distance.getValue(Pair(newReindeerPosition.pos, newReindeerPosition.dir)) >= current.cost + newReindeerPosition.cost) {

                    distance[Pair(newReindeerPosition.pos, newReindeerPosition.dir)] = current.cost + newReindeerPosition.cost

                    if (mazeMap[targetY][targetX] == 'E') {
                        if (current.cost + newReindeerPosition.cost <= lowest) {
                            lowest = current.cost + newReindeerPosition.cost
                            paths.addAll(newReindeerPosition.path)
                        }
                    } else {
                        pq.add(Reindeer(Position(targetX, targetY), current.cost + newReindeerPosition.cost, newReindeerPosition.dir, newReindeerPosition.path))
                    }
                }
            }
        }
    }

    return lowest to paths
}

fun costToMoveTo(target: Position, mazeMap: List<List<Char>>, current: Reindeer): Reindeer? {
    if (mazeMap[target.y][target.x] == '#') {
        return null
    }
    
    val newPath = current.path.toMutableList()
    newPath.add(target)
    
    return when (current.dir) {
        Direction.RIGHT -> {
            if (target.x > current.pos.x) {
                Reindeer(Position(target.x, target.y), 1, Direction.RIGHT, newPath)
            } else if (target.y < current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.UP, newPath)
            } else if (target.y > current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.DOWN, newPath)
            } else {
                null
            }
        }
        Direction.LEFT -> {
            if (target.x < current.pos.x) {
                Reindeer(Position(target.x, target.y), 1, Direction.LEFT, newPath)
            } else if (target.y < current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.UP, newPath)
            } else if (target.y > current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.DOWN, newPath)
            } else {
                null
            }
        }
        Direction.DOWN -> {
            if (target.y > current.pos.y) {
                Reindeer(Position(target.x, target.y), 1, Direction.DOWN, newPath)
            } else if (target.x < current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.LEFT, newPath)
            } else if (target.x > current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.RIGHT, newPath)
            } else {
                null
            }
        }
        Direction.UP -> {
            if (target.y < current.pos.y) {
                Reindeer(Position(target.x, target.y), 1, Direction.UP, newPath)
            } else if (target.x < current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.LEFT, newPath)
            } else if (target.x > current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.RIGHT, newPath)
            } else {
                null
            }
        }
    }
}

fun findStartCoordinate(lines: List<String>): Position? {
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell ->
            if (cell == 'S') {
                return Position(x, y)
            }
        }
    }
        
    return null
}

fun parseMazeMap(lines: List<String>): MutableList<MutableList<Char>> {
    val mazeMap = mutableListOf<MutableList<Char>>()
    
    lines.forEach { row ->
        val mazeRow = mutableListOf<Char>()
        row.forEach { cell ->
            mazeRow.add(cell)
        }
        mazeMap.add(mazeRow)
    }
    
    return mazeMap
}

data class Reindeer (val pos: Position, val cost: Int, val dir: Direction, val path: MutableList<Position>)

