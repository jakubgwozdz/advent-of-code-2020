package advent2020.day05

import advent2020.ProgressReceiver

fun decode(input: String) =
    input.fold(0) { a, c -> a * 2 + if (c == 'B' || c == 'R') 1 else 0 }


fun part1(input: String): String {
    val lines = input.trim().lines()
    val result = lines.map { decode(it) }.maxOrNull()!!
    return result.toString()
}

interface Day05Part2ProgressReceiver : ProgressReceiver {
    suspend fun foundSeat(no: Int, total: Int, seatId: Int, seatCode: String) {}
}

suspend fun part2(
    input: String,
    receiver: ProgressReceiver = object : Day05Part2ProgressReceiver {}
): String {
    receiver as Day05Part2ProgressReceiver
    val lines = input.trim().lines()
    val allSeats = lines.mapIndexed { i, v ->
        decode(v).also { receiver.foundSeat(i, lines.size, seatId = it, seatCode = v) }
    }.sorted()

    var leftId: Int? = null
    val rightId = allSeats.first { seatId -> (leftId == seatId - 2).also { leftId = seatId } }

    val result = rightId - 1

    return result.toString()

}

