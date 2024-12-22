fun day22 (lines: List<String>) {
    val startNumbers = lines.map { it.toLong() }
    
    val newSecretsSum = startNumbers
        .map { findNextSecretNumber(it, 1) }
        .sum()
    
    println("Day 22 part 1: $newSecretsSum")
}

fun findNextSecretNumber(secret: Long, depth: Int): Long {
    var newSecret = secret
    val level1 = secret * 64
    
    newSecret = prune(mix(level1, newSecret))
    
    val level2 = newSecret / 32
    
    newSecret = prune(mix(level2, newSecret))
    
    val level3 = newSecret * 2048
    
    newSecret = prune(mix(level3, newSecret))
    
    if (depth == 2000) {
        return newSecret
    }
    
    return findNextSecretNumber(newSecret, depth + 1)
}

fun mix(value: Long, secret: Long): Long {
    return value xor secret
}

fun prune(number: Long): Long {
    return number % 16777216
}
