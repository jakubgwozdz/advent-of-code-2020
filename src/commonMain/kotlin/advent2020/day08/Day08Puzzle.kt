package advent2020.day08

import advent2020.day08.OpCode.*
import advent2020.utils.binaryInsert

val regex by lazy { """(nop|acc|jmp) ([+\-])(\d+)""".toRegex() }

enum class OpCode { nop, acc, jmp, }
data class Instruction(val opCode: OpCode, val argument: Int)

private fun program(input: String): List<Instruction> = input.trim().lines()
    .map { regex.matchEntire(it)?.destructured ?: error("Invalid line $it") }
    .map { (op, sign, number) ->
        Instruction(OpCode.valueOf(op),
            if (sign == "-") -number.toInt() else number.toInt())
    }

class InfiniteLoopException(val ip: Int, val accumulator: Int) : Exception()

private fun runProgram(lines: List<Instruction>): Int {
    var accumulator = 0
    var ip = 0

    val visited = mutableListOf<Int>()
    while (ip != lines.size) {
        // if not visited, insert. If already visited - throw exception
        visited.binaryInsert(ip) { _, _ -> throw InfiniteLoopException(ip, accumulator) }
        when (lines[ip].opCode) {
            acc -> accumulator += lines[ip++].argument
            jmp -> ip += lines[ip].argument
            nop -> ip++
        }
    }
    return accumulator
}

fun part1(input: String): String {
    val lines = program(input)

    try {
        runProgram(lines)
        error("no infinite loop")
    } catch (e: InfiniteLoopException) {
        return e.accumulator.toString()
    }

}

fun part2(input: String): String {
    val lines = program(input)

    val result = lines.testWithChange(jmp, nop) ?: lines.testWithChange(nop, jmp) ?: error("no solution found")

    return result.toString()
}

private fun List<Instruction>.testWithChange(from: OpCode, to: OpCode) = asSequence()
    .mapIndexedNotNull { index, instruction -> if (instruction.opCode == from) index to instruction else null }
    .mapNotNull { (index, instruction) ->
        try {
            runProgram(withReplaced(index, instruction.copy(opCode = to)))
        } catch (e: InfiniteLoopException) {
            null
        }
    }
    .firstOrNull()

fun <E> List<E>.withReplaced(index: Int, value: E) = subList(0, index) + value + subList(index + 1, size)
