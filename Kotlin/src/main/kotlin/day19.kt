val computedPatterns = hashMapOf<String, Long>()

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
        .map { possiblePattern(towels, it) }
        .filter { it == true }
        .count()
    
    println("Day 19 part 1: $possiblePatterns")
    
    var combinations = 0L
    
    patterns.forEach { pattern ->
        combinations += findTowelCombinations(towels, pattern)
    }
    
    println("Day 19 part 2: $combinations")
}

fun possiblePattern(towels: List<String>, pattern: String): Boolean {
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

fun findTowelCombinations(towels: List<String>, pattern: String): Long {
    if (computedPatterns.containsKey(pattern)) {
        return computedPatterns[pattern]!!
    }
    
    var combinations = 0L
    
    towels.forEach { towel ->
        if (pattern.startsWith(towel)) {
            if (pattern.length == towel.length) {
                combinations += 1L
            } else {
                combinations += findTowelCombinations(towels, pattern.substring(towel.length))
            }
        }
    }
    
    computedPatterns[pattern] = combinations
    
    return combinations
}

data class TowelPattern(val fullPattern: String, val remainingPattern: String)
