package advent2020.day13

fun part1(input: String): String {
    val ts = input.trim().lines()[0].toInt()

    val wait = buses(input).map { it to it - ts % it }.minByOrNull { it.second }!!
    return (wait.first * wait.second).toString()
}

fun part2(input: String): String {

    var multiple = 1L
    var result = 0L
    busesWithOffsets(input).forEach { (bus, offset) ->
        while ((result + offset) % bus != 0L) result += multiple
        multiple *= bus
    }
    return result.toString()
}

private fun busesWithOffsets(input: String) = sequence {
    val line = input.trim().lines()[1].split(',')
    var offset = 0L
    line.forEach {
        if (it != "x") yield(it.toLong() to offset % it.toLong())
        offset++
    }
}

private fun buses(input: String) = busesWithOffsets(input).map {it.first}

