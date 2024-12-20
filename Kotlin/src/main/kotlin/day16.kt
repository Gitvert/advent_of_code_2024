import java.util.PriorityQueue

fun day16 (lines: List<String>) {
    val start = findStartCoordinate(lines)!!
    val end = findEndCoordinate(lines)!!
    
    val mazeMap = parseMazeMap(lines)
    val reindeer = Reindeer(start, 0, Direction.RIGHT)
    
    val part1Answer = findShortestPath(mazeMap, end, reindeer)
    
    println("Day 16 part 1: $part1Answer")
}

fun findAllShortestPaths(mazeMap: List<List<Char>>, end: Position, reindeer: Reindeer) {
    
}

fun findShortestPath(mazeMap: List<List<Char>>, end: Position, reindeer: Reindeer): Int {
    val dx = arrayOf(-1, 0, 1, 0)
    val dy = arrayOf(0, 1, 0, -1)
    val ySize = mazeMap.size
    val xSize = mazeMap[0].size
    val distance = Array(ySize) { IntArray(xSize) { Int.MAX_VALUE } }

    distance[reindeer.pos.y][reindeer.pos.x] = 0

    val pq: PriorityQueue<Reindeer> = PriorityQueue(xSize * ySize, compareBy { it.cost })

    pq.add(reindeer)

    while (pq.isNotEmpty()) {
        val current = pq.poll()

        for (i in 0..3) {
            val targetX = current.pos.x + dx[i]
            val targetY = current.pos.y + dy[i]

            val newReindeerPosition = costToMoveTo(Position(targetX, targetY), mazeMap, current)
            if (isInGrid(Position(targetX, targetY), xSize, ySize) && newReindeerPosition != null) {
                if (distance[targetY][targetX] > distance[current.pos.y][current.pos.x] + newReindeerPosition.cost) {

                    if (distance[targetX][targetX] != Int.MAX_VALUE) {
                        val adj = Reindeer(Position(targetX, targetY), distance[targetY][targetX], current.dir)
                        pq.remove(adj)
                    }

                    // Insert cell with updated distance
                    distance[targetY][targetX] = distance[current.pos.y][current.pos.x] + newReindeerPosition.cost
                    pq.add(Reindeer(Position(targetX, targetY), distance[targetY][targetX], newReindeerPosition.dir))
                }
            }
        }
    }

    return distance[end.y][end.x]
}

fun costToMoveTo(target: Position, mazeMap: List<List<Char>>, current: Reindeer): Reindeer? {
    if (mazeMap[target.y][target.x] == '#') {
        return null
    }
    
    return when (current.dir) {
        Direction.RIGHT -> {
            if (target.x > current.pos.x) {
                Reindeer(Position(target.x, target.y), 1, Direction.RIGHT)
            } else if (target.y < current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.UP)
            } else if (target.y > current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.DOWN)
            } else {
                null
            }
        }
        Direction.LEFT -> {
            if (target.x < current.pos.x) {
                Reindeer(Position(target.x, target.y), 1, Direction.LEFT)
            } else if (target.y < current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.UP)
            } else if (target.y > current.pos.y) {
                Reindeer(Position(target.x, target.y), 1001, Direction.DOWN)
            } else {
                null
            }
        }
        Direction.DOWN -> {
            if (target.y > current.pos.y) {
                Reindeer(Position(target.x, target.y), 1, Direction.DOWN)
            } else if (target.x < current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.LEFT)
            } else if (target.x > current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.RIGHT)
            } else {
                null
            }
        }
        Direction.UP -> {
            if (target.y < current.pos.y) {
                Reindeer(Position(target.x, target.y), 1, Direction.UP)
            } else if (target.x < current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.LEFT)
            } else if (target.x > current.pos.x) {
                Reindeer(Position(target.x, target.y), 1001, Direction.RIGHT)
            } else {
                null
            }
        }
    }
}

fun isInGrid(pos: Position, xSize: Int, ySize: Int): Boolean {
    return pos.x in 0 until xSize && pos.y in 0 until ySize
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

fun findEndCoordinate(lines: List<String>): Position? {
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell ->
            if (cell == 'E') {
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

data class Reindeer (val pos: Position, val cost: Int, val dir: Direction)
