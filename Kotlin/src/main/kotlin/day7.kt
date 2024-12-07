fun day7 (lines: List<String>) {
    day7part1(lines)
}

fun day7part1(lines: List<String>) {
    val equations = mutableListOf<Equation>()

    lines.forEach {
        val result = it.split(": ")[0].toLong()
        var numbers = it.split(": ")[1].split(" ").map { num -> num.toLong() }
        equations.add(Equation(result, numbers))
    }

    val answer = equations
        .filter { performOperation(Operation.ADD, it, 0, 0) || performOperation(Operation.MULTIPLY, it, 0, 0) }
        .sumOf{ it.result }

    println("Day 7 part 1: $answer")
}

fun performOperation(operation: Operation, equation: Equation, index: Int, result: Long): Boolean {
    if (!equation.numbers.indices.contains(index)) {
        return result == equation.result
    }

    val newResult: Long

    when(operation) {
        Operation.ADD -> {
            newResult = result + equation.numbers[index]
        }
        Operation.MULTIPLY -> {
            newResult = result * equation.numbers[index]
        }
    }

    if (newResult > equation.result) {
        return false
    }
    if (newResult == equation.result) {
        return true
    }
    return performOperation(Operation.ADD, equation, index + 1, newResult)
            || performOperation(Operation.MULTIPLY, equation, index + 1, newResult)
}

data class Equation (val result: Long, val numbers: List<Long>)

enum class Operation {
    ADD, MULTIPLY
}