package advent2020.day01

import advent2020.ProgressLogger

var comparisons = 0 // comparisons counter for reporting

internal fun findIndex(
    sortedEntries: List<Int>,
    expectedSum: Int,
    indexOfExcluded: Int = -1,
): Int {

    var startIndex = indexOfExcluded + 1
    var endIndex = sortedEntries.lastIndex

    while (startIndex < endIndex) {
        val sum = sortedEntries[startIndex] + sortedEntries[endIndex]
        comparisons++
        when {
            sum == expectedSum -> return startIndex
            sum < expectedSum -> startIndex++
            sum > expectedSum -> endIndex--
            else -> error("wtf")
        }
    }

    return -1
}

fun part1(input: String): String {
    comparisons = 0
    val lines = input.trim().lines()
    val sortedEntries = lines.map { it.toInt() }.sorted()

    val i1 = findIndex(sortedEntries, 2020)
    val v1 = sortedEntries[i1]
    val result = v1 * (2020 - v1)

    return result.toString()
}


interface Day01Part2ProgressLogger : ProgressLogger {
    suspend fun progress(no: Int, total: Int, entry: Int, comparisons: Int) {}
    suspend fun final(v1: Int, v2: Int, v3: Int, comparisons: Int) {}
}

suspend fun part2(input: String, logger: ProgressLogger = object : Day01Part2ProgressLogger {}): String {

    logger as Day01Part2ProgressLogger
    comparisons = 0
    val lines = input.trim().lines()
    val sortedEntries = lines.map { it.toInt() }.sorted()

    var i1 = -1
    var i2 = -1
    while (i2 < 0 && i1 < sortedEntries.size) {
        i1++
        logger.progress(i1 + 1, sortedEntries.size, sortedEntries[i1], comparisons)
        i2 = findIndex(sortedEntries, 2020 - sortedEntries[i1], i1)
    }
    val v1 = sortedEntries[i1]
    val v2 = sortedEntries[i2]
    val v3 = 2020 - v2 - v1
    val result = v1 * v2 * v3

    logger.final(v1, v2, v3, comparisons)
    return result.toString()
}
