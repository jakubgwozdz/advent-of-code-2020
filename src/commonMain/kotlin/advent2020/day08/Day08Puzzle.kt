package advent2020.day08

import advent2020.day08.OpCode.acc
import advent2020.day08.OpCode.jmp
import advent2020.day08.OpCode.nop

val regex = """(nop|acc|jmp) ([+\-])(\d+)""".toRegex()

enum class OpCode { nop, acc, jmp, }
data class Instruction(val opCode: OpCode, val argument: Int)

private fun program(input: String): List<Instruction> = input.trim().lines()
    .map { regex.matchEntire(it) ?: error("Invalid line $it") }
    .map { it.destructured }
    .map { (op, sign, number) ->
        Instruction(OpCode.valueOf(op),
            if (sign == "-") -number.toInt() else number.toInt())
    }

class InfiniteLoopException(val ip: Int, val accumulator: Int) : Exception()

private fun runProgram(lines: List<Instruction>): Int {
    var accumulator = 0
    var ip = 0

    val visited = mutableSetOf<Int>()

    while (ip != lines.size) {
        if (ip in visited) throw InfiniteLoopException(ip, accumulator)
        visited += ip
        when (lines[ip].opCode) {
            acc -> accumulator += lines[ip].argument
            jmp -> ip += lines[ip].argument - 1
            nop -> Unit
        }
        ip++
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

    repeat(input.length) { ip ->
        val line = lines[ip]
        when (line.opCode) {
            nop -> try {
                val result = runProgram(lines.withReplaced(ip, line.copy(opCode = jmp)))
                return result.toString()
            } catch (e: Exception) {
            }
            jmp -> try {
                val result = runProgram(lines.withReplaced(ip, line.copy(opCode = nop)))
                return result.toString()
            } catch (e: Exception) {
            }
            acc -> Unit
        }
    }

    error("no solution found")
}

fun <E> List<E>.withReplaced(index: Int, value: E) = subList(0, index) + value + subList(index + 1, size)
