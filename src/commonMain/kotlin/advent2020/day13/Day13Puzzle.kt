package advent2020.day13

fun part1(input: String): String {
    val lines = input.trim().lines()
    val ts = lines[0].toInt()
    val buses = lines[1].split(',').filterNot { it == "x" }.map { it.toInt() }

    val wait = buses.map { it to it - ts % it }.minByOrNull { it.second }!!
    return (wait.first * wait.second).toString()
}

fun part2(input: String): String {

    val buses = sequence {
        val line = input.trim().lines()[1].split(',')
        var offset = 0L
        line.forEach {
            if (it != "x") yield(it.toLong() to offset % it.toLong())
            offset++
        }
    }

    var multiple = 1L
    var result = 0L

    buses.forEach { (bus, offset) ->
        result = (0 until bus).asSequence().map { it * multiple + result }
            .first { (it + offset) % bus == 0L }
        multiple *= bus
    }
    return result.toString()
}

