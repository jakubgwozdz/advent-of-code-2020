package advent2020.day15

fun part1(input: String): String {
    val starting = input.trim().split(',').map { it.toInt() }

    return solve(starting, 2020).toString()
}

fun part2(input: String): String {
    val starting = input.trim().split(',').map { it.toInt() }

    return solve(starting, 30000000).toString()
}


fun solve(starting: List<Int>, turns: Int): Int {
    var turn = 0
    var prevNumber = 0
    val map = IntArray(turns + 1)
    starting.forEach { number ->
        turn++
        map[number] = turn
        prevNumber = number
    }

    while (turn < turns) {
        val prevTurn = map[prevNumber].let { if (it == 0) turn else it }
        val number = turn - prevTurn

        map[prevNumber] = turn
        turn++
        prevNumber = number

    }
    return prevNumber
}

