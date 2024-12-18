import kotlin.math.abs

val WIDTH = 101
val HEIGHT = 103

fun day14 (lines: List<String>) {
    val robots = mutableListOf<Robot>()
    
    lines.forEach { line ->
        val robot = Robot(
            line.split(" ")[0].split("=")[1].split(",")[0].toInt(),
            line.split(" ")[0].split("=")[1].split(",")[1].toInt(),
            line.split(" ")[1].split("=")[1].split(",")[0].toInt(),
            line.split(" ")[1].split("=")[1].split(",")[1].toInt(),
        )
        
        robots.add(robot)
    }

    day14part1(robots)
    day14part2(lines)
}

fun day14part1(robots: List<Robot>) {
    val seconds = 100
    val quadrant = Quadrant(0, 0, 0, 0)

    robots.forEach { robot ->
        robot.posX = robot.posX + (robot.dirX * seconds)
        robot.posY = robot.posY + (robot.dirY * seconds)

        if (robot.posX > WIDTH) {
            robot.posX = robot.posX % WIDTH
        }

        if (robot.posX < 0) {
            robot.posX = WIDTH - (abs(robot.posX) % WIDTH)
            if (robot.posX == WIDTH) {
                robot.posX = 0
            }
        }

        if (robot.posY > HEIGHT) {
            robot.posY = robot.posY % HEIGHT
        }

        if (robot.posY < 0) {
            robot.posY = HEIGHT - (abs(robot.posY) % HEIGHT)
            if (robot.posY == HEIGHT) {
                robot.posY = 0
            }
        }

        if (robot.posX < WIDTH / 2 && robot.posY < HEIGHT / 2) {
            quadrant.q1++
        } else if (robot.posX < WIDTH / 2 && robot.posY > HEIGHT / 2) {
            quadrant.q2++
        } else if (robot.posX > WIDTH / 2 && robot.posY < HEIGHT / 2) {
            quadrant.q3++
        } else if (robot.posX > WIDTH / 2 && robot.posY > HEIGHT / 2) {
            quadrant.q4++
        }
    }

    val answer = quadrant.q1 * quadrant.q2 * quadrant.q3 * quadrant.q4

    println("Day 14 part 1: $answer")
}

fun day14part2(lines: List<String>) {
    for (i in 0 until 10000) {
        val robots = mutableListOf<Robot>()
        

        lines.forEach { line ->
            val robot = Robot(
                line.split(" ")[0].split("=")[1].split(",")[0].toInt(),
                line.split(" ")[0].split("=")[1].split(",")[1].toInt(),
                line.split(" ")[1].split("=")[1].split(",")[0].toInt(),
                line.split(" ")[1].split("=")[1].split(",")[1].toInt(),
            )

            robots.add(robot)
        }
        
        waitXSeconds(robots.toList(), i)
        val uniquePositions = robots.map { Pair(it.posX, it.posY) }.toSet()
        if (uniquePositions.size == robots.size) {
            println("Day 14 part 2: $i")
        }
    }
}

fun waitXSeconds(robots: List<Robot>, seconds: Int) {

    robots.forEach { robot ->
        robot.posX = robot.posX + (robot.dirX * seconds)
        robot.posY = robot.posY + (robot.dirY * seconds)

        if (robot.posX > WIDTH) {
            robot.posX = robot.posX % WIDTH
        }

        if (robot.posX < 0) {
            robot.posX = WIDTH - (abs(robot.posX) % WIDTH)
            if (robot.posX == WIDTH) {
                robot.posX = 0
            }
        }

        if (robot.posY > HEIGHT) {
            robot.posY = robot.posY % HEIGHT
        }

        if (robot.posY < 0) {
            robot.posY = HEIGHT - (abs(robot.posY) % HEIGHT)
            if (robot.posY == HEIGHT) {
                robot.posY = 0
            }
        }

    }
}

data class Robot(var posX: Int, var posY: Int, val dirX: Int, val dirY: Int) 

data class Quadrant(var q1: Long, var q2: Long, var q3: Long, var q4: Long)