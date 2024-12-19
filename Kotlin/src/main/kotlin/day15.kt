import kotlin.math.abs

val DIRECTION_MAPPER = mapOf<Direction, Position>(
    Direction.LEFT to Position(-1, 0),
    Direction.RIGHT to Position(1, 0),
    Direction.UP to Position(0, -1),
    Direction.DOWN to Position(0, 1)
)

fun day15 (lines: List<String>) {
    val walls = mutableListOf<Position>()
    val boxes = mutableListOf<MutablePosition>()
    val moves = mutableListOf<Direction>()
    var robot = MutablePosition(0, 0)
    
    lines.forEachIndexed { y, line -> 
        line.forEachIndexed { x, cell ->
            if (cell == '#') {
                walls.add(Position(x, y))
            } else if (cell == 'O') {
                boxes.add(MutablePosition(x, y))
            } else if (cell == '@') {
                robot = MutablePosition(x, y)
            } else if (cell == '<') {
                moves.add(Direction.LEFT)
            } else if (cell == '>') {
                moves.add(Direction.RIGHT)
            } else if (cell == 'v') {
                moves.add(Direction.DOWN)
            } else if (cell == '^') {
                moves.add(Direction.UP)
            }
        }
    }
    printWarehouse(robot, walls, boxes)
    
    moves.forEach { move ->
        attemptToMove(robot, walls, boxes, move)
        //printWarehouse(robot, walls, boxes)
    }
    
    val answer = boxes.map { it.x.toLong() + it.y.toLong() * 100L }.sum()
    
    println("Day 15 part 1: $answer")
}

fun attemptToMove(robot: MutablePosition, walls: List<Position>, boxes: List<MutablePosition>, move: Direction) {
    val closestWall = findClosestWallInFront(robot, walls, move)
    if (getManhattanDistance(robot, closestWall) == 1) {
        return
    }

    val boxesInFront = findBoxesInFront(robot, boxes, move)
    if (boxesInFront.isEmpty() || getManhattanDistance(boxesInFront.first(), robot) > 1) {
        robot.x = robot.x + DIRECTION_MAPPER[move]!!.x
        robot.y = robot.y + DIRECTION_MAPPER[move]!!.y
        return
    }

    val boxesToMove = mutableListOf<MutablePosition>()

    boxesInFront.forEach { box ->
        if (boxesToMove.isEmpty()) {
            if (getManhattanDistance(box, closestWall) == 1) {
                return
            }
            boxesToMove.add(box)
        } else {
            if (getManhattanDistance(box, boxesToMove.last()) == 1) {
                boxesToMove.add(box)
                if (getManhattanDistance(box, closestWall) == 1) {
                    return
                }
            } else {
                robot.x = robot.x + DIRECTION_MAPPER[move]!!.x
                robot.y = robot.y + DIRECTION_MAPPER[move]!!.y
                boxesToMove.forEach { 
                    it.x = it.x + DIRECTION_MAPPER[move]!!.x
                    it.y = it.y + DIRECTION_MAPPER[move]!!.y
                }
                return
            }
        }
    }
    
    if (getManhattanDistance(boxesToMove.last(), closestWall) == 1) {
        return
    }

    robot.x = robot.x + DIRECTION_MAPPER[move]!!.x
    robot.y = robot.y + DIRECTION_MAPPER[move]!!.y
    boxesToMove.forEach {
        it.x = it.x + DIRECTION_MAPPER[move]!!.x
        it.y = it.y + DIRECTION_MAPPER[move]!!.y
    }
}

fun getManhattanDistance(p1: MutablePosition, p2: Position): Int {
    return abs(p1.x - p2.x) + abs(p1.y - p2.y)
}

fun getManhattanDistance(p1: MutablePosition, p2: MutablePosition): Int {
    return abs(p1.x - p2.x) + abs(p1.y - p2.y)
}

fun findBoxesInFront(robot: MutablePosition, boxes: List<MutablePosition>, move: Direction): List<MutablePosition> {
    return when (move) {
        Direction.UP -> {
            boxes.filter { it.y < robot.y && it.x == robot.x }.sortedByDescending { it.y }
        }
        Direction.DOWN -> {
            boxes.filter { it.y > robot.y && it.x == robot.x }.sortedBy { it.y }
        }
        Direction.LEFT -> {
            boxes.filter { it.x < robot.x && it.y == robot.y }.sortedByDescending { it.x }
        }
        Direction.RIGHT -> {
            boxes.filter { it.x > robot.x && it.y == robot.y }.sortedBy { it.x }
        }
    }
}

fun findClosestWallInFront(robot: MutablePosition, walls: List<Position>, move: Direction): Position {
    return when (move) {
        Direction.UP -> {
            walls.filter { it.y < robot.y && it.x == robot.x }.maxBy { it.y }
        }
        Direction.DOWN -> {
            walls.filter { it.y > robot.y && it.x == robot.x }.minBy { it.y }
        }
        Direction.LEFT -> {
            walls.filter {it.x < robot.x && it.y == robot.y }.maxBy { it.x }
        }
        Direction.RIGHT -> {
            walls.filter {it.x > robot.x && it.y == robot.y }.minBy { it.x }
        }
    }
}

fun printWarehouse(robot: MutablePosition, walls: List<Position>, boxes: List<MutablePosition>) {
    println()
    for (y in 0 until 10) {
        for (x in 0 until 10) {
            if (robot.x == x && robot.y == y) {
                print('@')
            } else if (walls.contains(Position(x, y))) {
                print('#')
            } else if (boxes.contains(MutablePosition(x, y))) {
                print('O')
            } else {
                print('.')
            }
        }
        println()
    }
}

data class MutablePosition(var x: Int, var y: Int)
