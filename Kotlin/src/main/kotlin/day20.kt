import kotlin.math.abs

fun day20 (lines: List<String>) {
    val walls = mutableListOf<Position>()
    var start = Position(0, 0)
    var end = Position(0, 0)
    
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell -> 
            if (cell == '#') {
                walls.add(Position(x, y))
            } else if (cell == 'S') {
                start = Position(x,y)
            } else if (cell == 'E') {
                end = Position(x,y)
            }
        }
    }
    
    val path = findPath(walls, start, end)
    var validCheats = 0
    
    for (i in 0 .. path.indices.last) {
        for (j in i .. path.indices.last) {
            val first = path[i]
            val second = path[j]
            
            if ((first.x == second.x && abs(first.y - second.y) == 2) || (first.y == second.y && abs(first.x - second.x) == 2)) {
                val firstIndex = path.indexOf(first)
                val secondSecond = path.indexOf(second)
                
                if (abs(firstIndex - secondSecond) >= 101) {
                    validCheats++
                }
            }
        }
    }
    
    println("Day 20 part 1: $validCheats")
}

fun findPath(walls: List<Position>, start: Position, end: Position): List<Position> {

    var current = start
    val path = mutableListOf(current)
    
    while (current != end) {
        val next = findNextPosition(walls, current, path)!!
        path.add(next)
        current = next
    }
    
    return path
}

fun findNextPosition(walls: List<Position>, current: Position, path: MutableList<Position>): Position? {
    val dx = arrayOf(-1, 0, 1, 0)
    val dy = arrayOf(0, 1, 0, -1)
    
    for (i in 0 until 4) {
        if (!walls.contains(Position(current.x + dx[i], current.y + dy[i]))) {
            if (!path.contains(Position(current.x + dx[i], current.y + dy[i]))) {
                return Position(current.x + dx[i], current.y + dy[i])
            }
        }
    }
    
    return null
}