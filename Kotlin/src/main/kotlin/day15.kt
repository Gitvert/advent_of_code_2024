import kotlin.math.abs

val DIRECTION_MAPPER = mapOf<Direction, Position>(
    Direction.LEFT to Position(-1, 0),
    Direction.RIGHT to Position(1, 0),
    Direction.UP to Position(0, -1),
    Direction.DOWN to Position(0, 1)
)

fun day15 (lines: List<String>) {
    day15part1(lines)
    day15part2(lines)
}

fun day15part2(lines: List<String>) {
    val walls = mutableListOf<Position>()
    val boxes = mutableListOf<MutablePosition>()
    val moves = mutableListOf<Direction>()
    var robot = MutablePosition(0, 0)

    lines.forEachIndexed { y, line ->
        line.replace("#", "##").replace("O","[]").replace(".","..").replace("@","@.").forEachIndexed { x, cell ->
            if (cell == '#') {
                walls.add(Position(x, y))
            } else if (cell == '[') {
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

    moves.forEach { move ->
        attemptToMoveInBigWarehouse(robot, walls, boxes, move)
    }

    val answer = boxes.map { it.x.toLong() + it.y.toLong() * 100L }.sum()

    println("Day 15 part 2: $answer")
}

fun attemptToMoveInBigWarehouse(robot: MutablePosition, walls: List<Position>, boxes: List<MutablePosition>, move: Direction) {
    if (blockedByWall(robot, walls, DIRECTION_MAPPER[move]!!)) {
        return
    }

    val adjacentBox = findAdjacentBox(robot, boxes, move)
    
    if (adjacentBox == null) {
        robot.x = robot.x + DIRECTION_MAPPER[move]!!.x
        robot.y = robot.y + DIRECTION_MAPPER[move]!!.y
        return
    }

    val boxesToMove = mutableSetOf<MutablePosition>()
    if (tryToPushBox(walls, boxes, move, adjacentBox, boxesToMove)) {
        robot.x = robot.x + DIRECTION_MAPPER[move]!!.x
        robot.y = robot.y + DIRECTION_MAPPER[move]!!.y
        boxesToMove.forEach {
            it.x = it.x + DIRECTION_MAPPER[move]!!.x
            it.y = it.y + DIRECTION_MAPPER[move]!!.y
        }
    }
}

fun tryToPushBox(walls: List<Position>, boxes: List<MutablePosition>, move: Direction, box: MutablePosition, boxesToMove: MutableSet<MutablePosition>): Boolean {
    if (boxesToMove.contains(box)) {
        return true
    }
    
    boxesToMove.add(box)
    when (move) {
        Direction.LEFT -> {
            if (walls.any { it.y == box.y && it.x + 1 == box.x }) {
                return false
            } else if (boxes.any { it.y == box.y && it.x + 2 == box.x }) {
                return tryToPushBox(walls, boxes, move, boxes.find { it.y == box.y && it.x + 2 == box.x }!!, boxesToMove)
            } else {
                return true
            }
        }
        Direction.RIGHT -> {
            if (walls.any { it.y == box.y && it.x - 2 == box.x }) {
                return false
            } else if (boxes.any { it.y == box.y && it.x - 2 == box.x }) {
                return tryToPushBox(walls, boxes, move, boxes.find { it.y == box.y  && it.x - 2 == box.x }!!, boxesToMove)
            } else {
                return true
            }
        }
        Direction.UP -> {
            if (walls.any { (it.y + 1 == box.y && it.x == box.x) || (it.y + 1 == box.y && it.x - 1 == box.x) }) {
                return false
            }
            
            val topLeft = boxes.find { it.x + 1 == box.x && it.y + 1 == box.y }
            val topMid = boxes.find { it.x == box.x && it.y + 1 == box.y }
            val topRight = boxes.find { it.x - 1 == box.x && it.y + 1 == box.y }
            
            if (topLeft != null && topRight != null) {
                return tryToPushBox(walls, boxes, move, topLeft, boxesToMove) && tryToPushBox(walls, boxes, move, topRight, boxesToMove)
            } else if (topLeft != null) {
                return tryToPushBox(walls, boxes, move, topLeft, boxesToMove)
            } else if (topMid != null) {
                return tryToPushBox(walls, boxes, move, topMid, boxesToMove)
            } else if (topRight != null) {
                return tryToPushBox(walls, boxes, move, topRight, boxesToMove)
            } else {
                return true
            }
        }
        Direction.DOWN -> {
            if (walls.any { (it.y - 1 == box.y && it.x == box.x) || (it.y - 1 == box.y && it.x - 1 == box.x) }) {
                return false
            }

            val bottomLeft = boxes.find { it.x + 1 == box.x && it.y - 1 == box.y }
            val bottomMid = boxes.find { it.x == box.x && it.y - 1 == box.y }
            val bottomRight = boxes.find { it.x - 1 == box.x && it.y - 1 == box.y }

            if (bottomLeft != null && bottomRight != null) {
                return tryToPushBox(walls, boxes, move, bottomLeft, boxesToMove) && tryToPushBox(walls, boxes, move, bottomRight, boxesToMove)
            } else if (bottomLeft != null) {
                return tryToPushBox(walls, boxes, move, bottomLeft, boxesToMove)
            } else if (bottomMid != null) {
                return tryToPushBox(walls, boxes, move, bottomMid, boxesToMove)
            } else if (bottomRight != null) {
                return tryToPushBox(walls, boxes, move, bottomRight, boxesToMove)
            } else {
                return true
            }
        }
    }
}

fun blockedByWall(robot: MutablePosition, walls: List<Position>, dir: Position): Boolean {
    return walls.any { it.x == robot.x + dir.x && it.y == robot.y + dir.y }
}

fun findAdjacentBox(robot: MutablePosition, boxes: List<MutablePosition>, move: Direction): MutablePosition? {
    return when (move) {
        Direction.UP -> {
            boxes.find { (it.x == robot.x && it.y + 1 == robot.y) || (it.x + 1 == robot.x && it.y + 1 == robot.y) }
        }
        Direction.DOWN -> {
            boxes.find { (it.x == robot.x && it.y - 1 == robot.y) || (it.x + 1 == robot.x && it.y - 1 == robot.y) }
        }
        Direction.LEFT -> {
            boxes.find { it.y == robot.y && it.x + 2 == robot.x }
        }
        Direction.RIGHT -> {
            boxes.find { it.y == robot.y && it.x - 1 == robot.x }
        }
    } 
}

fun day15part1(lines: List<String>) {
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

    moves.forEach { move ->
        attemptToMove(robot, walls, boxes, move)
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

data class MutablePosition(var x: Int, var y: Int)
