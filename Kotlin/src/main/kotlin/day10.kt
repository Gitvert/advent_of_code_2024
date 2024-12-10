fun day10 (lines: List<String>) {
    val map = mutableListOf<MutableList<Int>>()
    
    lines.forEach { line -> 
        val row = mutableListOf<Int>()
        line.forEach { cell -> 
            row.add(cell.toString().toInt())
        }
        map.add(row)
    }
    
    val trailStarts = mutableListOf<TrailCoordinate>()
    
    map.forEachIndexed { y, row -> 
        row.forEachIndexed { x, cell ->
            if (cell == 0) {
                trailStarts.add(TrailCoordinate(x, y))
            }
        }
    }
    
    day10part1(map, trailStarts)
    day10part2()
}

fun day10part1(map: List<List<Int>>, trailStarts: List<TrailCoordinate>) {
    var answer = 0
    
    trailStarts.forEach{
        val peaks = mutableSetOf<TrailCoordinate>()
        findTrailCount(map, it, 0, peaks)
        answer += peaks.size
    }
    
    println("Day 10 part 1: $answer")
}

fun day10part2() {

}

fun findTrailCount(map: List<List<Int>>, position: TrailCoordinate, targetHeight: Int, peaks: MutableSet<TrailCoordinate>) {
    if (position.x < 0 || position.y < 0 || position.x >= map.size || position.y >= map.size) { 
        return 
    }
    
    if (map[position.y][position.x] != targetHeight) { 
        return 
    }
    
    if (map[position.y][position.x] == 9) {
        peaks.add(TrailCoordinate(position.x, position.y))
        return 
    }
    
    findTrailCount(map, TrailCoordinate(position.x + 1, position.y), targetHeight + 1, peaks)
    findTrailCount(map, TrailCoordinate(position.x - 1, position.y), targetHeight + 1, peaks)
    findTrailCount(map, TrailCoordinate(position.x, position.y + 1), targetHeight + 1, peaks)
    findTrailCount(map, TrailCoordinate(position.x, position.y - 1), targetHeight + 1, peaks)
}

data class TrailCoordinate(val x: Int, val y: Int)