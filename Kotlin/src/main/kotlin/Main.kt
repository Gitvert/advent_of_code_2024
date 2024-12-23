import java.io.File
import kotlin.system.measureTimeMillis

val daySolvers = listOf(::day1, ::day2, ::day3, ::day4, ::day5, ::day6, ::day7, ::day8, ::day9, ::day10, ::day11, ::day12, ::day13, ::day14, ::day15, ::day16, ::day17, ::day18, ::day19, ::day20, ::day21, ::day22, ::day23, ::day24, ::day25)

val skipDays = listOf(6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 22, 23)

fun main(args: Array<String>) {
    val timings = mutableMapOf<String, Long>()

    for (i in 1..daySolvers.size) {
        if (skipDays.contains(i)) {
            continue
        }
        
        val timeTaken = measureTimeMillis {
            daySolvers[i-1](readFile("day$i.txt"))
        }

        timings["day$i"] = timeTaken
    }

    /*println("Total execution time was: ${timings.values.sum()} ms")
    println()
    println("Execution time for each day was: ")

    timings.toList().sortedByDescending { it.second }.forEach {
        println("${it.first}: ${it.second} ms")
    }*/
}

fun readFile(fileName: String): List<String> {
    val lines: MutableList<String> = mutableListOf()

    File("../inputs/$fileName").useLines { line -> line.forEach { lines.add(it) } }

    return lines
}