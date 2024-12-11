fun day11 (lines: List<String>) {

    day11part1(lines)
    day11part2(lines)
}

fun day11part1(lines: List<String>) {
    val numbers = lines[0].split(" ")

    var currentNumbers = numbers.toMutableList()
    
    for (i in 0 until 25) {
        val newNumbers = mutableListOf<String>()
        
        currentNumbers.forEach { 
            if (it == "0") {
                newNumbers.add("1")
            } else if (it.length % 2 == 0) {
                val mid = it.length / 2
                newNumbers.add(it.substring(0, mid))
                newNumbers.add(it.substring(mid).toLong().toString())
            } else {
                newNumbers.add((it.toLong() * 2024).toString())
            }
        }
        
        currentNumbers = newNumbers.toMutableList()
    }
    
    println("Day 11 part 1: ${currentNumbers.size}")
}

fun day11part2(lines: List<String>) {
    val numbers = lines[0].split(" ")
    var currentNumbersMap = mutableMapOf<String, Long>()
    
    numbers.forEach {
        currentNumbersMap.put(it, 1)
    }

    for (i in 0 until 75) {
        var newNumbersMap = mutableMapOf<String, Long>()
        
        currentNumbersMap.forEach { 
            if (it.key == "0") {
                newNumbersMap["1"] = (newNumbersMap["1"] ?: 0) + it.value
            } else if (it.key.length % 2 == 0) {
                val mid = it.key.length / 2
                newNumbersMap[it.key.substring(0, mid).toLong().toString()] = (newNumbersMap[it.key.substring(0, mid).toLong().toString()] ?: 0) + it.value
                newNumbersMap[it.key.substring(mid).toLong().toString()] = (newNumbersMap[it.key.substring(mid).toLong().toString()] ?: 0) + it.value
            } else {
                newNumbersMap[(it.key.toLong() * 2024).toString()] = (newNumbersMap[(it.key.toLong() * 2024).toString()] ?: 0) + it.value
            }
        }

        currentNumbersMap = newNumbersMap.toMutableMap()
    }
    
    var part2Answer = 0L
    
    currentNumbersMap.forEach { 
        part2Answer += it.value
    }

    println("Day 11 part 2: $part2Answer")
}