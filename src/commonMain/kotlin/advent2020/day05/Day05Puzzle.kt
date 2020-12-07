package advent2020.day05

import advent2020.ProgressLogger

fun decode(input: String) =
    input.fold(0) { a, c -> a * 2 + if (c == 'B' || c == 'R') 1 else 0 }


fun part1(input: String): String {
    val lines = input.trim().lines()
    val result = lines.map { decode(it) }.maxOrNull()!!
    return result.toString()
}

interface Day05Part2ProgressLogger : ProgressLogger {
    suspend fun foundSeat(no: Int, total: Int, seatId: Int, seatCode: String) {}
}

suspend fun part2(
    input: String,
    logger: ProgressLogger = object : Day05Part2ProgressLogger {}
): String {
    logger as Day05Part2ProgressLogger
    val lines = input.trim().lines()
    val allSeats = lines.mapIndexed { i, v ->
        decode(v).also { logger.foundSeat(i, lines.size, seatId = it, seatCode = v) }
    }.sorted()

    var leftId: Int? = null
    val rightId = allSeats.first { seatId -> (leftId == seatId - 2).also { leftId = seatId } }

    val result = rightId - 1

    return result.toString()

}

