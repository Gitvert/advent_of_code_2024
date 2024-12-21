fun day19 (lines: List<String>) {
    val towels = mutableListOf<String>()
    val patterns = mutableListOf<String>()
    
    lines.forEach { 
        if (it.contains(',')) {
            towels.addAll(it.split(", "))
        } else if (it.isNotEmpty()) {
            patterns.add(it)
        }
    }

    val possiblePatterns = patterns
        .map { containsTowel(towels, it) }
        .filter { it == true }
        .count()
    
    println("Day 19 part 1: $possiblePatterns")
}

fun containsTowel(towels: List<String>, pattern: String): Boolean {
    val queue = mutableSetOf<TowelPattern>()
    
    queue.add(TowelPattern(pattern, pattern))
    
    while (queue.isNotEmpty()) {
        val towelPattern = queue.first()
        queue.remove(towelPattern)
        
        towels.forEach { towel ->
            if (towelPattern.remainingPattern.startsWith(towel)) {
                if (towelPattern.remainingPattern.length == towel.length) {
                    return true
                } else {
                    queue.add(TowelPattern(towelPattern.fullPattern, towelPattern.remainingPattern.substring(towel.length)))
                }
            }
        }
    }
    
    return false
}

data class TowelPattern(val fullPattern: String, val remainingPattern: String)