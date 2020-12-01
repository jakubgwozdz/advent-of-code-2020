package advent2020.day01

import advent2020.ProgressReceiver
import advent2020.emptyReceiver
import kotlinx.coroutines.delay

interface Day01Part2ProgressReceiver : ProgressReceiver {
    suspend fun progress(no: Int, total: Int, entry: Int, comparisons: Int) {}
    suspend fun final(v1: Int, v2: Int, v3: Int, comparisons: Int) {}
}

suspend fun part1(input: String, receiver: ProgressReceiver = emptyReceiver): String {
    val lines = input.trim().lines()
    val numbers = lines.map { it.toInt() }.sorted()
    val i1 = findIndex(numbers, 2020)
    val v1 = numbers[i1]
    val result = v1 * (2020 - v1)
    return result.toString()
}

internal fun findIndex(
    sortedEntries: List<Int>,
    expectedSum: Int,
    indexOfExcluded: Int = -1
): Int {
    // first, edge case if half the value exists twice
    val indexOfHalf = sortedEntries.binarySearch(expectedSum / 2)
    if (indexOfHalf > 0) {
        val v1 = sortedEntries[indexOfHalf]
        if (indexOfHalf > 0 && v1 + sortedEntries[indexOfHalf - 1] == expectedSum) return indexOfHalf
        if (indexOfHalf < sortedEntries.size - 1 && v1 + sortedEntries[indexOfHalf + 1] == expectedSum) return indexOfHalf
    }

    // now, edge case if excluded value existed twice, in such case we simply don't exclude at all
    val updatedExcluded = if (indexOfExcluded < 0) null else {
        if (indexOfExcluded > 0 && sortedEntries[indexOfExcluded] == sortedEntries[indexOfExcluded - 1]) null
        else if (indexOfExcluded < sortedEntries.size - 1 && sortedEntries[indexOfExcluded] == sortedEntries[indexOfExcluded + 1]) null
        else sortedEntries[indexOfExcluded]
    }

    // now the normal search
    return sortedEntries.asSequence()
        .takeWhile { it < expectedSum / 2 }
        .indexOfFirst { it != updatedExcluded && sortedEntries.bsContains(expectedSum - it) }
}

var comparisons = 0 // comparisons counter for reporting

suspend fun part2(input: String, receiver: ProgressReceiver = object : Day01Part2ProgressReceiver{}): String {
    val lines = input.trim().lines()
    val numbers = lines.map { it.toInt() }.sorted()
    var no = 0
    comparisons = 0

    var i2 = -1

    val i1 = numbers.indexOfFirst {
        (receiver as Day01Part2ProgressReceiver).progress(no + 1, numbers.size, it, comparisons)
        delay(receiver.delay)
        i2 = findIndex(numbers, 2020 - it, no)
        no++
        i2 >= 0
    }
    val v1 = numbers[i1]
    val v2 = numbers[i2]
    val v3 = 2020 - v2 - v1
    (receiver as Day01Part2ProgressReceiver).final(v1, v2, v3, comparisons)
    val result = v1 * v2 * v3
    return result.toString()
}

// binary-search based .contains()
private fun List<Int>.binarySearch(value: Int) = binarySearch { v ->
    comparisons++
    v.compareTo(value)
}

// binary-search based .contains()
private fun List<Int>.bsContains(value: Int) = binarySearch(value) >= 0

private suspend fun report(receiver: ProgressReceiver, no: Int, size: Int, value: Int, comparisons: Int) {
    if (receiver is Day01Part2ProgressReceiver) {
        receiver.progress(no, size, value, comparisons)
        delay(receiver.delay)
    }
}

private suspend fun final(receiver: ProgressReceiver, v1: Int, v2: Int, v3: Int, comparisons: Int) {
    if (receiver is Day01Part2ProgressReceiver) {
        receiver.final(v1, v2, v3, comparisons)
    }
}

