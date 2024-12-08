fun day8 (lines: List<String>) {
    val antennas = mutableMapOf<Char, MutableList<Antenna>>()
    
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, cell ->
            if (cell != '.') {
                antennas.computeIfAbsent(cell) { mutableListOf() }.add(Antenna(cell, Coord(x, y)))
            }
        }
    }
    
    day8part1(antennas, lines.size)
    day8part2(antennas, lines.size)
}

fun day8part1(antennas: MutableMap<Char, MutableList<Antenna>>, maxSize: Int) {
    val antiNodes = mutableSetOf<Coord>()
    
    antennas.forEach { _, value ->
        antiNodes.addAll(findAntiNodes(value, 1, maxSize))
    }
    
    val answer = antiNodes.filter { it.x >= 0 && it.x < maxSize && it.y >= 0 && it.y < maxSize }.size
    
    println("Day 8 part 1: $answer")
}

fun day8part2(antennas: MutableMap<Char, MutableList<Antenna>>, maxSize: Int) {
    val antiNodes = mutableSetOf<Coord>()

    antennas.forEach { _, value ->
        antiNodes.addAll(value.map{ it.coord })
        antiNodes.addAll(findAntiNodes(value, 30, maxSize))
    }

    val answer = antiNodes.size

    println("Day 8 part 2: $answer")
}

fun findAntiNodes(antennas: List<Antenna>, maxIterations: Int, maxSize: Int): Set<Coord> {
    val antiNodes = mutableSetOf<Coord>()
    
    for (i in antennas.indices) {
        for (j in antennas.indices) {
            if (i == j || j < i) {
                continue
            }
            
            val first = antennas[i]
            val second = antennas[j]
            val deltaX = second.coord.x - first.coord.x
            val deltaY = second.coord.y - first.coord.y
            
            var iteration = 1
            
            while (iteration <= maxIterations) {
                val xLeft = first.coord.x - (deltaX * iteration)
                val yLeft = first.coord.y - (deltaY * iteration)

                val xRight = second.coord.x + (deltaX * iteration)
                val yRight = second.coord.y + (deltaY * iteration)
                
                if (xLeft >= 0 && xLeft < maxSize && yLeft >= 0 && yLeft < maxSize) {
                    antiNodes.add(Coord(xLeft, yLeft))
                }
                if (xRight >= 0 && yRight < maxSize && yRight >= 0 && xRight < maxSize) {
                    antiNodes.add(Coord(xRight, yRight))
                }
                
                iteration++
            }
        }
    }
    
    return antiNodes
}

data class Coord(val x: Int, val y: Int)

data class Antenna(val frequency: Char, val coord: Coord)