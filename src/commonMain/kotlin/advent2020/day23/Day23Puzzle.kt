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

class Node(val cup: Int) {
    lateinit var next: Node
}

class Circle(input: String, val noOfCups: Int) {
    val cups = IntArray(noOfCups) { if (it < input.length) "${input[it]}".toInt() else it + 1 }
    val cup123 = IntArray(3)
    var currentIdx = 0
    operator fun get(i: Int) = cups[pos(i + currentIdx)]

    private fun pos(i: Int) = (i % noOfCups).let { if (it < 0) noOfCups + it else it }
    fun indexOf(cup: Int): Int = cups.indexOf(cup)

    fun round(destIdx: Int) {
        val current = cups[currentIdx]
        cups.copyInto(cup123, 0, 1, 4)
        cups.copyInto(cups, 0, 4, destIdx + 1)
        cup123.copyInto(cups, destIdx - 3)
        cups.copyInto(cups, destIdx, destIdx + 1)
        cups[cups.size - 1] = current
    }

    fun dest(): Int {
        val current = cups[currentIdx]

        var destination = current - 1
        if (destination == 0) destination = noOfCups
        while (destination == cups[currentIdx + 1] || destination == cups[currentIdx + 2] || destination == cups[currentIdx + 3]) {
            destination--
            if (destination == 0) destination = noOfCups
        }
        return destination

    }

    override fun toString() = cups.joinToString(" ", "[", "]", limit = 100) { "$it".padStart(2) }
}

fun game(input: String, noOfCups: Int = input.length, times: Int): IntArray {
    val circle = Circle(input, noOfCups)

    val timer = Monotonic.markNow()
    var nextPrint = 10.seconds

    repeat(times) { move ->
        val destination = circle.dest()
        val index = circle.indexOf(destination)
        println("${"$move".padStart(3)}: $circle $destination $index")
        circle.round(index)

        val elapsed = timer.elapsedNow()
        if (elapsed > nextPrint) {
            println("$elapsed: $move of $times moves (${(move * 100.0 / times).toInt()}%), ETA in ${elapsed * times / (move + 1) - elapsed}")
            nextPrint += 10.seconds
        }
    }
    println("${"$times".padStart(3)}: $circle")
    return circle.cups
}

