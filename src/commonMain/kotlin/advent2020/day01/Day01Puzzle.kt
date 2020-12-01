package advent2020.day01

import advent2020.ProgressReceiver

interface Day01Part1ProgressReporter : ProgressReceiver {
}

interface Day01Part2ProgressReporter : ProgressReceiver {
}

suspend fun part1(input: String, receiver: ProgressReceiver): String {
    val lines = input.trim().lines()
    val numbers = lines.map { it.toInt() }.toSet()
    val result = findEntries(numbers, 2020, emptySet())
    return result.toString()
}

private fun findEntries(entries: Set<Int>, sum: Int, excluded: Set<Int>): Int? {
    val found = entries.firstOrNull { it !in excluded && (sum - it) in entries } ?: return null
    val result = found * (sum - found) * excluded.fold(1) { a, b -> a * b }
    return result
}

suspend fun part2(input: String, receiver: ProgressReceiver): String {
    val lines = input.trim().lines()
    val numbers = lines.map { it.toInt() }.toSet()
    val v1 = numbers.first { findEntries(numbers, 2020-it, setOf(it))!=null }.also { println(it) }
    val result = findEntries(numbers, 2020-v1, setOf(v1))!!
    return result.toString()
}

