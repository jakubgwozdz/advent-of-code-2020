package advent2020.day03

import advent2020.ProgressReceiver

interface DayO3ProgressReceiver : ProgressReceiver {
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

suspend fun part2(input: String, progressReceiver: ProgressReceiver = object : DayO3ProgressReceiver {}): String {
    progressReceiver as DayO3ProgressReceiver
    val lines = input.trim().lines()

    progressReceiver.reset(lines)
    val result = moves.map { countTrees(lines, it, progressReceiver) }.reduce { a, b -> a * b }

    return result.toString()
}

private suspend fun countTrees(
    lines: List<String>,
    move: Vector,
    progressReceiver: ProgressReceiver = object : DayO3ProgressReceiver {},
): Long {
    progressReceiver as DayO3ProgressReceiver
    progressReceiver.reset(lines, move)

    return lines
        .filterIndexed { y, line ->
            val (right, down) = move
            if (y % down != 0) return@filterIndexed false
            val x = y / down * right
            val x2 = x % line.length
            progressReceiver.moveTo(x, y, move)
            line[x2] == '#'
        }
        .count().toLong()
        .also { progressReceiver.totalCollisions(it, move) }
}

