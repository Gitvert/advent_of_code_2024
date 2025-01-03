import kotlin.math.pow

fun day17 (lines: List<String>) {
    day17part1(lines)
    test(lines)
    //day17part2(lines)
}

fun day17part2(lines: List<String>) {
    val test = "4700747000000016".toLong(8)
    
    val programState = ProgramState(0, test, 0, 0, listOf(), mutableListOf())

    lines.forEach { line ->
        if (line.contains("Program: ")) {
            programState.program = line.split("Program: ")[1].split(",").map { it.toLong() }
        }
    }

    try {
        while (true) {
            val instructionPointerStart = programState.instructionPointer

            performInstruction(
                programState.program[programState.instructionPointer.toInt()],
                programState.program[(programState.instructionPointer + 1).toInt()],
                programState
            )

            val instructionPointerEnd = programState.instructionPointer

            if (instructionPointerStart == instructionPointerEnd) {
                programState.instructionPointer += 2
            }
        }
    } catch (_: Exception) {
        println("Day 17 part 2: ${programState.output.joinToString(",")}")
    }
}

fun test(lines: List<String>) {
    var programDefinition = listOf<Long>()
    
    lines.forEach { line ->
        if (line.contains("Program: ")) {
            programDefinition = line.split("Program: ")[1].split(",").map { it.toLong() }
        }
    }
    
    var index = 4L
    while (true) {
        index *= 2
        val programState = ProgramState(0, index, 0, 0, programDefinition, mutableListOf())

        try {
            while (true) {
                val instructionPointerStart = programState.instructionPointer

                performInstruction(
                    programState.program[programState.instructionPointer.toInt()],
                    programState.program[(programState.instructionPointer + 1).toInt()],
                    programState
                )

                val instructionPointerEnd = programState.instructionPointer

                if (instructionPointerStart == instructionPointerEnd) {
                    programState.instructionPointer += 2
                }
            }
        } catch (_: Exception) {
            println("${index.toString(8)}: ${programState.output.joinToString(",")}")
            println("${index}: ${programState.output.joinToString(",")}")
            if (programState.program == programState.output) {
                println("Day 17 part 2: $index") // Too low: 26530000000L
                break
            }
        }
    }
}

fun day17part1(lines: List<String>) {
    val programState = ProgramState(0, 46323429, 0, 0, listOf(), mutableListOf())

    lines.forEach { line ->
        if (line.contains("Program: ")) {
            programState.program = line.split("Program: ")[1].split(",").map { it.toLong() }
        }
    }

    try {
        while (true) {
            val instructionPointerStart = programState.instructionPointer

            performInstruction(
                programState.program[programState.instructionPointer.toInt()],
                programState.program[(programState.instructionPointer + 1).toInt()],
                programState
            )

            val instructionPointerEnd = programState.instructionPointer

            if (instructionPointerStart == instructionPointerEnd) {
                programState.instructionPointer += 2
            }
        }
    } catch (_: Exception) {
        println("Day 17 part 1: ${programState.output.joinToString(",")}")
    }
}

fun performInstruction(opcode: Long, operand: Long, programState: ProgramState) {
    when (opcode) {
        0L -> programState.registerA = programState.registerA / (2L.toDouble().pow(getComboValue(operand, programState).toDouble())).toLong()
        1L -> programState.registerB = programState.registerB.xor(operand)
        2L -> programState.registerB = getComboValue(operand, programState) % 8
        3L -> {
            if (programState.registerA != 0L) {
                programState.instructionPointer = operand
            }
        }
        4L -> programState.registerB = programState.registerB.xor(programState.registerC)
        5L -> programState.output.add(getComboValue(operand, programState) % 8)
        6L -> programState.registerB = programState.registerA / (2L.toDouble().pow(getComboValue(operand, programState).toDouble())).toLong()
        7L -> programState.registerC = programState.registerA / (2L.toDouble().pow(getComboValue(operand, programState).toDouble())).toLong()
        else -> throw RuntimeException("Invalid opcode $opcode")
    }
}

fun getComboValue(operand: Long, programState: ProgramState): Long {
    return when (operand) {
        0L -> 0L
        1L -> 1L
        2L -> 2L
        3L -> 3L
        4L -> programState.registerA
        5L -> programState.registerB
        6L -> programState.registerC
        else -> throw RuntimeException("Invalid operand $operand")
    }
}

data class ProgramState(var instructionPointer: Long, var registerA: Long, var registerB: Long, var registerC: Long, var program: List<Long>, val output: MutableList<Long>)