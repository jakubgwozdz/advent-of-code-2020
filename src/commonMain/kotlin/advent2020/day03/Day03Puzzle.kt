package advent2020.day03

import advent2020.ProgressLogger

interface DayO3ProgressLogger : ProgressLogger {
    suspend fun reset(lines: List<String>) {}
    suspend fun reset(lines: List<String>, move: Vector) {}
    suspend fun moveTo(x: Int, y: Int, move: Vector) {}
    suspend fun totalCollisions(count: Long, move: Vector) {}
}

suspend fun part1(input: String): String {
    val lines = input.trim().lines()

    val result = countTrees(lines, part1move)

    return result.toString()
}

typealias Vector = Pair<Int, Int>

val part1move: Vector = 3 to 1
val moves: List<Vector> = listOf(1 to 1, part1move, 5 to 1, 7 to 1, 1 to 2)

suspend fun part2(input: String, progressLogger: ProgressLogger = object : DayO3ProgressLogger {}): String {
    progressLogger as DayO3ProgressLogger
    val lines = input.trim().lines()

    progressLogger.reset(lines)
    val result = moves.map { countTrees(lines, it, progressLogger) }.reduce { a, b -> a * b }

    return result.toString()
}

private suspend fun countTrees(
    lines: List<String>,
    move: Vector,
    progressLogger: ProgressLogger = object : DayO3ProgressLogger {},
): Long {
    progressLogger as DayO3ProgressLogger
    progressLogger.reset(lines, move)

    return lines
        .filterIndexed { y, line ->
            val (right, down) = move
            if (y % down != 0) return@filterIndexed false
            val x = y / down * right
            val x2 = x % line.length
            progressLogger.moveTo(x, y, move)
            line[x2] == '#'
        }
        .count().toLong()
        .also { progressLogger.totalCollisions(it, move) }
}

