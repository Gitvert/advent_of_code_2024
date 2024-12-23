fun day23 (lines: List<String>) {
    val connections = mutableListOf<Pair<String, String>>()
    
    lines.forEach {
        connections.add(Pair(it.split('-')[0], it.split('-')[1]))
        connections.add(Pair(it.split('-')[1], it.split('-')[0]))
    }
    
    val nodes = mutableListOf<Node>()
    
    connections.forEach { connection ->
        if (nodes.find { it.name == connection.first } != null) {
            nodes.find { it.name == connection.first }!!.neighbors.add(connection.second)
        } else {
            nodes.add(Node(connection.first, mutableListOf(connection.second)))
        }
    }
    
    val triplets = mutableSetOf<List<String>>()
    
    nodes.forEach { node ->
        for (i in 0 until node.neighbors.size) {
            for (j in i until node.neighbors.size) {
                if (connections.contains(Pair(node.neighbors[i], node.neighbors[j]))) {
                    val triplet = mutableListOf<String>()
                    triplet.add(node.name)
                    triplet.add(node.neighbors[i])
                    triplet.add(node.neighbors[j])
                    
                    triplets.add(triplet.sorted())
                }
            }
        }
    }
    
    val part1Answer = triplets
        .filter { triplet -> triplet.filter { it.startsWith("t") }.isNotEmpty() }
        .count()
    
    println("Day 23 part 1: $part1Answer")

    var largerClique = triplets.toSet()
    var largestClique = 3
    
    try {
        while (true) {
            val newClique = createLargerClique(largerClique, nodes)
            val newCliqueSize = newClique.first().size
            if (newCliqueSize > largestClique) {
                largerClique = newClique
                largestClique = newCliqueSize
            } else {
                break
            }
        }
    } catch (_: Exception) { }
    
    val part2Answer = largerClique.joinToString(",").replace(" ", "")
    
    println("Day 23 part 2: $part2Answer")
}

fun createLargerClique(currentClique: Set<List<String>>, nodes: MutableList<Node>): Set<List<String>> {
    val largerClique = mutableSetOf<List<String>>()
    
    currentClique.forEach { clique ->
        nodes.forEach { node ->
            if (node.neighbors.containsAll(clique)) {
                val newClique = clique.toMutableList()
                newClique.add(node.name)
                largerClique.add(newClique.sorted())
            }
        }
    }
    
    return largerClique
}

data class Node(val name: String, val neighbors: MutableList<String>)