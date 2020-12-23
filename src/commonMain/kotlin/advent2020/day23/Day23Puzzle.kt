package advent2020.day23

import kotlin.time.TimeSource.Monotonic
import kotlin.time.seconds

fun part1(input: String): String {
    val cups = game(input.trim(), times = 100)
    return part1resultFormat(cups)
}

fun part1resultFormat(cups: IntArray): String {
    val indexOf1 = cups.indexOf(1)
    return (cups.drop(indexOf1 + 1) + cups.take(indexOf1)).joinToString("")
}

fun part2(input: String): String {
    val cups = game(input.trim(), noOfCups = 1000000, times = 10000000)
    return part2resultFormat(cups)
}

fun part2resultFormat(cups: IntArray): String {
    val indexOf1 = cups.indexOf(1)
    val first = cups[(indexOf1 + 1) % cups.size]
    val second = cups[(indexOf1 + 2) % cups.size]
    val result = (first.toLong() * second.toLong()).toString()
    println("$first * $second = $result")
    return result
}

fun game(input: String, noOfCups: Int = input.length, times: Int): IntArray {
    val cups = IntArray(noOfCups) { if (it < input.length) "${input[it]}".toInt() else it + 1 }
    val cup123 = IntArray(3)

    val timer = Monotonic.markNow()
    var nextPrint = 10.seconds

    repeat(times) { move ->
        val current = cups[0]

        var destination = current - 1
        while (destination == cups[1] || destination == cups[2] || destination == cups[3] || destination < 1) {
            destination--
            if (destination < 1) destination = noOfCups
        }
        val index = cups.indexOf(destination)
        println("${"$move".padStart(3)}: ${cups.joinToString(" ", "[", "]") { "$it".padStart(2) }}")
        cups.copyInto(cup123, 0, 1, 4)
        cups.copyInto(cups, 0, 4, index + 1)
        cup123.copyInto(cups, index - 3)
        cups.copyInto(cups, index, index + 1)
        cups[cups.size - 1] = current
        val elapsed = timer.elapsedNow()
        if (elapsed > nextPrint) {
            println("$elapsed: $move of $times moves (${(move * 100.0 / times).toInt()}%), ETA in ${elapsed * times / (move + 1) - elapsed}")
            nextPrint += 10.seconds
        }
    }
    println("${"$times".padStart(3)}: ${cups.joinToString(" ", "[", "]") { "$it".padStart(2) }}")
    return cups
}

