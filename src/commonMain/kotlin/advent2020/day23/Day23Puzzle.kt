package advent2020.day23

import kotlin.time.TimeSource.Monotonic
import kotlin.time.seconds

fun part1(input: String): String {
    val start = input.trim().lines().single().map { "$it".toInt() }
    val cups = game(start, 100)

    val indexOf1 = cups.indexOf(1)
    val result = cups.drop(indexOf1 + 1) + cups.take(indexOf1)

    return result.joinToString("")
}

fun part2(input: String): String {
    val inputCups = input.trim().lines().single().map { "$it".toInt() }
    val cups = IntArray(1000000) { it + 1 }.toMutableList()
    repeat(inputCups.size) { cups[it] = inputCups[it] }

    game(cups, 10000000)

    val indexOf1 = cups.indexOf(1)
    val result = cups[(indexOf1 + 1) % cups.size].toLong() * cups[(indexOf1 + 2) % cups.size].toLong()

    return result.toString()
}

private fun game(start: List<Int>, times: Int): List<Int> {
    val cups = start.toMutableList()

    var current = cups.first()

    val timer = Monotonic.markNow()
    var nextPrint = 10.seconds

    repeat(times) { move ->

        val removed = mutableListOf<Int>()
        removed += cups.removeAt((cups.indexOf(current) + 1) % cups.size)
        removed += cups.removeAt((cups.indexOf(current) + 1) % cups.size)
        removed += cups.removeAt((cups.indexOf(current) + 1) % cups.size)

        val max = cups.maxOrNull()!!
        val min = cups.minOrNull()!!
        var destination = current - 1
        while (destination !in cups) {
            destination--
            if (destination < min) destination = max
        }

        cups.addAll(cups.indexOf(destination) + 1, removed)

        current = cups[(cups.indexOf(current) + 1) % cups.size]

        val elapsed = timer.elapsedNow()
        if (elapsed > nextPrint) {
            println("$elapsed: $move moves")
            nextPrint += 10.seconds
        }
    }
    return cups.toList()
}

