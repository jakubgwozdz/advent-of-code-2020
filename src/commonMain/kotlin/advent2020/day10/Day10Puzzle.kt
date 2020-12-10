package advent2020.day10

import advent2020.utils.groups

fun part1(input: String): String {
    val adapters = input.trim().lines().map(String::toLong) + 0L

    val sorted = adapters.sorted()

    var ones = 0
    var threes = 1 // last one is 3

    (1 until sorted.size).forEach { index ->
        val diff = (sorted[index] - sorted[index - 1]).toInt()
        if (diff == 3) threes++
        if (diff == 1) ones++
    }

    return (ones * threes).toString()
}

fun part2(input: String): String {
    val adapters = (input.trim().lines().map(String::toLong) + 0L)

    val sorted = adapters.sorted()
    val distances = sorted.indices.map {
        if (it == sorted.size - 1) 3
        else (sorted[it + 1] - sorted[it]).toInt()
    }

    val count = distances.asSequence().groups { it == 3 }
        .filter { it.isNotEmpty() }
        .map { chunk -> chunk.ways() }
        .fold(1L) { acc, e -> acc * e }

    return count.toString()
}

val cache = mutableMapOf<Pair<List<Int>, Int>, Int>()

fun List<Int>.ways(s: Int = 0): Int = cache.getOrPut(this to s) {
    when {
        s >= size - 1 -> 1
        this[s] == 2 && this[s + 1] == 2 -> this.ways(s + 1)
        s < size - 2 && (0..2).all { this[s + it] == 1 } -> (1..3).map { this.ways(s + it) }.sum()
        else /* any other more than two-elem */ -> (1..2).map { this.ways(s + it) }.sum()
    }
}
