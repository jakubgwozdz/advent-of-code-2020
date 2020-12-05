package advent2020.day05

fun decode(input: String): Int {
    return input.asSequence().fold(0) { a, c -> a * 2 + if (c == 'B' || c == 'R') 1 else 0 }
}


fun part1(input: String): String {
    val lines = input.trim().lines()
    val result = lines.map { decode(it) }.maxOrNull()!!
    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lines()
    val allSeats = lines.map { decode(it) }.sorted()

    var leftId: Int? = null
    val rightId = allSeats.first { seatId -> (leftId == seatId - 2).also { leftId = seatId } }

    val result = rightId - 1

    return result.toString()

}

