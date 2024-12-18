import kotlin.math.abs

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
    
    val width = 101
    val height = 103
    val seconds = 100
    val quadrant = Quadrant(0, 0, 0, 0)
    
    robots.forEach { robot -> 
        robot.posX = robot.posX + (robot.dirX * seconds)
        robot.posY = robot.posY + (robot.dirY * seconds)
        
        if (robot.posX > width) {
            robot.posX = robot.posX % width
        }
        
        if (robot.posX < 0) {
            robot.posX = width - (abs(robot.posX) % width)
            if (robot.posX == width) {
                robot.posX = 0
            }
        }

        if (robot.posY > height) {
            robot.posY = robot.posY % height
        }

        if (robot.posY < 0) {
            robot.posY = height - (abs(robot.posY) % height)
            if (robot.posY == height) {
                robot.posY = 0
            }
        }
        
        if (robot.posX < width / 2 && robot.posY < height / 2) {
            quadrant.q1++
        } else if (robot.posX < width / 2 && robot.posY > height / 2) {
            quadrant.q2++
        } else if (robot.posX > width / 2 && robot.posY < height / 2) {
            quadrant.q3++
        } else if (robot.posX > width / 2 && robot.posY > height / 2) {
            quadrant.q4++
        }
    }
    
    val answer = quadrant.q1 * quadrant.q2 * quadrant.q3 * quadrant.q4
    
    println("Day 14 part 1: $answer")
}

data class Robot(var posX: Int, var posY: Int, val dirX: Int, val dirY: Int) 

data class Quadrant(var q1: Long, var q2: Long, var q3: Long, var q4: Long)