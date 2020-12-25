package advent2020.day25

fun part1(input: String): String {
    val lines = input.trim().lines()
    val cardPK = lines[0].toLong()
        .also { println(" cardPK $it; ") }
    val doorPK = lines[1].toLong()
        .also { println(" doorPK $it; ") }

    val cardLoop = calcLoop(cardPK)
        .also { println(" cardLoop $it; ") }

    val doorLoop = calcLoop(doorPK)
        .also { println(" doorLoop $it; ") }


    TODO()
}

private fun calcLoop(devicePK: Long): Long {
    var result = 0L
    while (true) {
        if (transform(7, result) == devicePK)
            return result
        else result++
    }
}

private fun transform(subject: Long, loopSize: Long): Long {
    var result = 1L
    repeat(loopSize.toInt()) {
        result *= subject
        result %= 20201227
    }
    return result
}

fun part2(input: String): String {
    val lines = input.trim().lines()

    TODO()
}

