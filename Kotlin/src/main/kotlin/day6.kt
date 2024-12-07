fun day6 (lines: List<String>) {
    println()
    day6part1(lines)
    day6part2(lines)
}

fun day6part1(lines: List<String>) {
    val obstaclePositions = mutableListOf<Position>()
    var guard = Guard(Position(0,0), Direction.UP)

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, cell ->
            if (cell == '#') {
                obstaclePositions.add(Position(x, y))
            } else if (cell == '^') {
                guard.position = Position(x, y)
            }
        }
    }

    val size = lines.size
    val visitedPositions = mutableSetOf(guard.position)
    val previousGuardStates = mutableSetOf(Guard(guard.position, guard.direction))

    while (inBounds(size, guard)) {
        while (turnIfNeeded(guard, obstaclePositions)) {
            previousGuardStates.add(Guard(guard.position, guard.direction))
        }
        move(guard)
        visitedPositions.add(guard.position)
        previousGuardStates.add(Guard(guard.position, guard.direction))
    }

    println("Day 6 part 1: ${visitedPositions.size - 1}")
}

fun day6part2(lines: List<String>) {
    val obstaclePositions = mutableListOf<Position>()
    var guard = Guard(Position(0,0), Direction.UP)

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, cell ->
            if (cell == '#') {
                obstaclePositions.add(Position(x, y))
            } else if (cell == '^') {
                guard.position = Position(x, y)
            }
        }
    }

    val size = lines.size
    var options = 0

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, cell ->
            if (cell == '.') {
                val enhancedObstaclePositions = obstaclePositions.toMutableList()
                enhancedObstaclePositions.add(Position(x, y))
                val guardCopy = Guard(Position(guard.position.x, guard.position.y), guard.direction)
                var steps = 0

                while (inBounds(size, guardCopy)) {
                    while (turnIfNeeded(guardCopy, enhancedObstaclePositions)) {
                    }
                    move(guardCopy)
                    steps++
                    if (steps > 7000) {
                        options++
                        break
                    }
                }
            }
        }
    }

    println("Day 6 part 2: $options")
}

fun inBounds(size: Int, guard: Guard): Boolean {
    if (guard.position.x < 0) {
        return false
    }

    if (guard.position.y < 0) {
        return false
    }

    if (guard.position.x == size) {
        return false
    }

    if (guard.position.y == size) {
        return false
    }

    return true
}

fun move(guard: Guard) {
    when (guard.direction) {
        Direction.DOWN -> guard.position = Position(guard.position.x, guard.position.y + 1)
        Direction.UP -> guard.position = Position(guard.position.x, guard.position.y - 1)
        Direction.LEFT -> guard.position = Position(guard.position.x - 1, guard.position.y)
        Direction.RIGHT -> guard.position = Position(guard.position.x + 1, guard.position.y)
    }
}

fun turnIfNeeded(guard: Guard, obstaclePositions: MutableList<Position>): Boolean {
    when (guard.direction) {
        Direction.DOWN -> {
            if (obstaclePositions.contains(Position(guard.position.x, guard.position.y + 1))) {
                guard.direction = Direction.LEFT
                return true
            }
        }
        Direction.UP -> {
            if (obstaclePositions.contains(Position(guard.position.x, guard.position.y - 1))) {
                guard.direction = Direction.RIGHT
                return true
            }
        }
        Direction.LEFT -> {
            if (obstaclePositions.contains(Position(guard.position.x - 1, guard.position.y))) {
                guard.direction = Direction.UP
                return true
            }
        }
        Direction.RIGHT -> {
            if (obstaclePositions.contains(Position(guard.position.x + 1, guard.position.y))) {
                guard.direction = Direction.DOWN
                return true
            }
        }
    }
    return false
}

data class Position (val x: Int, val y: Int)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class Guard (var position: Position, var direction: Direction)