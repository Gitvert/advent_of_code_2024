fun day7 (lines: List<String>) {
    val equations = mutableListOf<Equation>()

    lines.forEach {
        val result = it.split(": ")[0].toLong()
        var numbers = it.split(": ")[1].split(" ").map { num -> num.toLong() }
        equations.add(Equation(result, numbers))
    }

    day7part1(equations)
    day7part2(equations)
}

fun day7part1(equations: List<Equation>) {
    val answer = equations
        .filter { performOperation(Operation.ADD, it, 0, 0, false) || performOperation(Operation.MULTIPLY, it, 0, 0, false) }
        .sumOf{ it.result }

    println("Day 7 part 1: $answer")
}

fun day7part2(equations: List<Equation>) {
    val answer = equations
        .filter {
            performOperation(Operation.ADD, it, 0, 0, true)
                || performOperation(Operation.MULTIPLY, it, 0, 0, true)
                || performOperation(Operation.CONCAT, it, 0, 0, true)
        }
        .sumOf{ it.result }

    println("Day 7 part 2: $answer")
}

fun performOperation(operation: Operation, equation: Equation, index: Int, result: Long, part2: Boolean): Boolean {
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
        Operation.CONCAT -> {
            if (!part2) {
                return false
            } else {
                newResult = (result.toString() + equation.numbers[index].toString()).toLong()
            }
        }
    }

    if (newResult > equation.result) {
        return false
    }
    if (newResult == equation.result) {
        return true
    }
    return performOperation(Operation.ADD, equation, index + 1, newResult, part2)
        || performOperation(Operation.MULTIPLY, equation, index + 1, newResult, part2)
        || performOperation(Operation.CONCAT, equation, index + 1, newResult, part2)
}

data class Equation (val result: Long, val numbers: List<Long>)

enum class Operation {
    ADD, MULTIPLY, CONCAT
}