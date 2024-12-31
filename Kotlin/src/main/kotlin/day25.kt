fun day25 (lines: List<String>) {
    var schematic = mutableListOf<String>()
    val schematics = mutableListOf<Schematic>()

    lines.forEach { line ->
        if (line.isEmpty()) {
            val type = if (schematic.first() == "#####") {
                SchematicType.LOCK
            } else {
                SchematicType.KEY
            }
            schematics.add(Schematic(type, schematic.toList(), listOf()))
            schematic = mutableListOf()
        } else {
            schematic.add(line)
        }
    }

    val type = if (schematic.first() == "#####") {
        SchematicType.LOCK
    } else {
        SchematicType.KEY
    }
    schematics.add(Schematic(type, schematic.toList(), listOf()))
    schematic = mutableListOf()

    schematics.forEach { 
        val pins = getPinHeightList(it)
        it.pins = pins
    }

    val locks = schematics.filter { it.type == SchematicType.LOCK }
    val keys = schematics.filter { it.type == SchematicType.KEY }
    
    var okPairs = 0
    
    locks.forEach { lock -> 
        keys.forEach { key ->
            val combinedHeights = listOf(
                key.pins[0] + lock.pins[0],
                key.pins[1] + lock.pins[1],
                key.pins[2] + lock.pins[2],
                key.pins[3] + lock.pins[3],
                key.pins[4] + lock.pins[4],
            )
            
            if (combinedHeights.all { it <= 5 }) {
                okPairs++
            }
        }
    }
    
    println("Day 25 part 1: $okPairs")
}

fun getPinHeightList(schematic: Schematic): List<Int> {
    val rowToSkip = if (schematic.type == SchematicType.KEY) { 6 } else { 0 }
    
    val pins = mutableListOf(0, 0, 0, 0, 0)
    
    for (i in schematic.schematic.indices) {
        if (i == rowToSkip) {
            continue
        }
        
        val line = schematic.schematic[i]
        line.forEachIndexed { index, s -> 
            if (s == '#') {
                pins[index] = pins[index] + 1
            }
        }
    }
    
    return pins
}

data class Schematic(val type: SchematicType, val schematic: List<String>, var pins: List<Int>)

enum class SchematicType {
    KEY,
    LOCK
}