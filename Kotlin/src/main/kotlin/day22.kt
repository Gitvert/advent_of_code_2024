fun day22 (lines: List<String>) {
    val startNumbers = lines.map { it.toLong() }
    
    val allSequences = mutableListOf<Sequence>()
    val allSequenceCostMaps = mutableListOf(mapOf<Sequence, Long>())
    var secretsSum = 0L

    allSequences.clear()
    allSequenceCostMaps.clear()
    
    startNumbers.forEachIndexed { index, startNumber ->
        val sequences = mutableListOf<SequenceWithCost>()
        secretsSum += findNextSecretNumber(startNumber, 1, sequences) 
        allSequences.addAll(sequences.filter { it.fourth != null }.map {Sequence(it.first, it.second, it.third, it.fourth)})
        
        val sequenceCostMap = mutableMapOf<Sequence, Long>()
        sequences.filter { it.fourth != null }.forEach { s -> 
            val sequence = Sequence(s.first, s.second, s.third, s.fourth)
            if (!sequenceCostMap.containsKey(sequence)) {
                sequenceCostMap[sequence] = s.price
            }
        }
        
        allSequenceCostMaps.add(sequenceCostMap)
    }

    println("Day 22 part 1: $secretsSum")
    
    val allSequencesSet = allSequences.toSet()
    var highestPrice = Long.MIN_VALUE

    allSequencesSet.forEach { sequence ->
        var sequencePrice = 0L
        allSequenceCostMaps.forEach { costMap ->
            sequencePrice += costMap.getOrDefault(sequence, 0L)
        }
        
        if (sequencePrice > highestPrice) {
            highestPrice = sequencePrice
        }
    }

    println("Day 22 part 2: $highestPrice")
}

fun findNextSecretNumber(secret: Long, depth: Int, sequences: MutableList<SequenceWithCost>): Long {
    var newSecret = secret
    val level1 = secret * 64
    
    newSecret = prune(mix(level1, newSecret))
    
    val level2 = newSecret / 32
    
    newSecret = prune(mix(level2, newSecret))
    
    val level3 = newSecret * 2048
    
    newSecret = prune(mix(level3, newSecret))
    
    val oldSecretLastDigit = secret % 10
    val newSecretLastDigit = newSecret % 10
    
    val diff = newSecretLastDigit - oldSecretLastDigit
    sequences.add(getNewSequence(sequences.lastOrNull(), diff, newSecretLastDigit))
    
    if (depth == 2000) {
        return newSecret
    }
    
    return findNextSecretNumber(newSecret, depth + 1, sequences)
}

fun getNewSequence(sequence: SequenceWithCost?, diff: Long, price: Long ): SequenceWithCost {
    if (sequence == null) {
        return SequenceWithCost(diff, null, null, null, price)
    }
    
    return if (sequence.first == null) {
        SequenceWithCost(diff, null, null, null, price)
    } else if (sequence.second == null) {
        SequenceWithCost(sequence.first, diff, null, null, price)
    } else if (sequence.third == null) {
        SequenceWithCost(sequence.first, sequence.second, diff, null, price)
    } else if (sequence.fourth == null) {
        SequenceWithCost(sequence.first, sequence.second, sequence.third, diff, price)
    } else {
        SequenceWithCost(sequence.second, sequence.third, sequence.fourth, diff, price)
    }
}

fun mix(value: Long, secret: Long): Long {
    return value xor secret
}

fun prune(number: Long): Long {
    return number % 16777216
}

data class SequenceWithCost(val first: Long?, val second: Long?, val third: Long?, val fourth: Long?, val price: Long)

data class Sequence(val first: Long?, val second: Long?, val third: Long?, val fourth: Long?)
