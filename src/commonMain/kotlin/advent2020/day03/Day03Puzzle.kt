package advent2020.day03

import advent2020.ProgressReceiver
import advent2020.emptyReceiver

interface Day03Part1ProgressReporter : ProgressReceiver {
}

interface Day03Part2ProgressReporter : ProgressReceiver {
}


suspend fun part1(input: String, receiver: ProgressReceiver = emptyReceiver): String {
    val lines = input.trim().lines()

    val move: (Int, String) -> Boolean = { index, line -> line[index * 3 % line.length] == '#' }

    val result = trees(lines, move)

    return result.toString()
}

suspend fun part2(input: String, receiver: ProgressReceiver = emptyReceiver): String {
    val lines = input.trim().lines()

    val moves = listOf<(Int, String) -> Boolean>(
        { index, line -> line[index % line.length] == '#' },
        { index, line -> line[index * 3 % line.length] == '#' },
        { index, line -> line[index * 5 % line.length] == '#' },
        { index, line -> line[index * 7 % line.length] == '#' },
        { index, line -> index % 2 == 0 && line[index / 2 % line.length] == '#' },
    )

    val result = moves.map { trees(lines, it) }.reduce(Long::times)

    return result.toString()
}

private fun trees(
    lines: List<String>,
    move: (Int, String) -> Boolean
) = lines.filterIndexed(move).count().toLong()

