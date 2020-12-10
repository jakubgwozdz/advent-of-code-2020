package advent2020.day10

import advent2020.utils.groups

fun part1(input: String): String {

    val sorted = (input.trim().lines().map(String::toLong) + 0L).sorted()

    var ones = 0
    var threes = 0

    (0 until sorted.lastIndex).forEach { index ->
        val diff = (sorted[index+1] - sorted[index]).toInt()
        if (diff == 1) ones++
        if (diff == 3) threes++
    }
    threes++ // at the end there is 3 to the device

    return (ones * threes).toString()
}

fun part2(input: String): String {

    val sorted = (input.trim().lines().map(String::toLong) + 0L).sorted()
    val distances = (0 until sorted.lastIndex).map {
        (sorted[it + 1] - sorted[it]).toInt()
    }

    val count = distances.asSequence().groups { it == 3 }
        .map { chunk -> chunk.ways() }
        .fold(1L) { acc, e -> acc * e }

    return count.toString()
}

private fun List<Int>.twoTwosAt(i: Int) = size >= i + 2 && this[i] == 2 && this[i + 1] == 2
private fun List<Int>.threeOneAt(i: Int) = size >= i + 3 && this[i] == 1 && this[i + 1] == 1 && this[i + 2] == 1

val cache = mutableMapOf<List<Int>, Int>()

internal fun List<Int>.ways(): Int = cache.getOrPut(this) {
    var result = 0
    val toCheck = mutableListOf(0)

    while (toCheck.isNotEmpty()) {
        val start = toCheck.removeFirst()
        when {
            start >= size - 1 -> result++
            twoTwosAt(start) -> toCheck.add(start + 1)
            threeOneAt(start) -> (1..3).map { toCheck.add(start + it) }
            else -> (1..2).map { toCheck.add(start + it) }
        }
    }
    result
}
