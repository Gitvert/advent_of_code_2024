fun day12 (lines: List<String>) {
    val garden = mutableListOf<MutableList<Char>>()

    lines.forEach { line ->
        val row = mutableListOf<Char>()
        line.forEach { cell ->
            row.add(cell)
        }
        garden.add(row)
    }
    
    val size = garden.size
    val regions = mutableListOf<Region>()
    val visited = mutableSetOf<PlotPosition>()
    
    for (y in 0 until size) {
        for (x in 0 until size) {
            if (!visited.contains(PlotPosition(x, y))) {
                val region = Region(mutableSetOf())
                region.plots.add(Plot(garden[y][x], PlotPosition(x, y)))
                findNeighbors(garden, PlotPosition(x, y), size, garden[y][x], visited, region)
                regions.add(region)
            }
        }
    }
    
    var fenceCost = 0
    
    regions.forEach { region ->
        var perimeter = 0
        
        region.plots.forEach { plot -> 
            perimeter += calculatePerimeter(garden, plot, size)
        }
        
        fenceCost += perimeter * region.plots.size
    }
    
    println("Day 12 part 1: $fenceCost")
}

fun findNeighbors(garden: List<List<Char>>, pos: PlotPosition, size: Int, plantType: Char, visited: MutableSet<PlotPosition>, region: Region) {
    if (visited.contains(pos)) {
        return
    }
    
    visited.add(pos)
    
    if (pos.x - 1 >= 0 && garden[pos.y][pos.x - 1] == plantType) {
        val newPlot = Plot(plantType, PlotPosition(pos.x - 1, pos.y))
        region.plots.add(newPlot)
        findNeighbors(garden, newPlot.pos, size, plantType, visited, region)
    }
    
    if (pos.x + 1 < size && garden[pos.y][pos.x + 1] == plantType) {
        val newPlot = Plot(plantType, PlotPosition(pos.x + 1, pos.y))
        region.plots.add(newPlot)
        findNeighbors(garden, newPlot.pos, size, plantType, visited, region)
    }
    
    if (pos.y - 1 >= 0 && garden[pos.y - 1][pos.x] == plantType) {
        val newPlot = Plot(plantType, PlotPosition(pos.x, pos.y - 1))
        region.plots.add(newPlot)
        findNeighbors(garden, newPlot.pos, size, plantType, visited, region)
    }
    
    if (pos.y + 1 < size && garden[pos.y + 1][pos.x] == plantType) {
        val newPlot = Plot(plantType, PlotPosition(pos.x, pos.y + 1))
        region.plots.add(newPlot)
        findNeighbors(garden, newPlot.pos, size, plantType, visited, region)
    }
}

fun calculatePerimeter(garden: List<List<Char>>, plot: Plot, size: Int): Int {
    var perimeter = 4
    
    if (plot.pos.y - 1 >= 0 && garden[plot.pos.y - 1][plot.pos.x] == plot.plantType) {
        perimeter--
    }
    
    if (plot.pos.y + 1 < size && garden[plot.pos.y + 1][plot.pos.x] == plot.plantType) {
        perimeter--
    }
    
    if (plot.pos.x - 1 >= 0 && garden[plot.pos.y][plot.pos.x - 1] == plot.plantType) {
        perimeter--
    }
    
    if (plot.pos.x + 1 < size && garden[plot.pos.y][plot.pos.x + 1] == plot.plantType) {
        perimeter--
    }
    
    return perimeter
}

data class PlotPosition(val x: Int, val y: Int)

data class Plot(val plantType: Char, val pos: PlotPosition)

data class Region(val plots: MutableSet<Plot>)