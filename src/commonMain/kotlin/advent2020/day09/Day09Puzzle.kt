package advent2020.day09

import advent2020.ProgressLogger

internal fun String.parsedData() = trim().lines().map(String::toLong)

fun valid(preamble: List<Long>, expectedSum: Long): Boolean {

    val sortedPreamble = preamble.sorted()
    var startIndex = 0
    var endIndex = sortedPreamble.lastIndex

    while (startIndex < endIndex) {
        val sum = sortedPreamble[startIndex] + sortedPreamble[endIndex]
        when {
            sum < expectedSum -> startIndex++
            sum > expectedSum -> endIndex--
            else -> return true
        }
    }

    return false
}

fun firstInvalid(data: List<Long>, preambleLength: Int = 25) = (preambleLength..data.lastIndex)
    .first { index ->
        val preamble = data.subList(index - preambleLength, index)
        (!valid(preamble, data[index]))
    }
    .let { data[it] }

fun List<Long>.minPlusMax(): Long {
    var minimum = this[0]
    var maximum = this[0]
    forEach {
        if (it < minimum) minimum = it
        if (it > maximum) maximum = it
    }
    return minimum + maximum
}

interface Day09ProgressLogger : ProgressLogger {
    suspend fun startingSearch(data: List<Long>, start: Int, end: Int, sum: Long, expectedSum: Long) {}
    suspend fun expanding(start: Int, end: Int, sum: Long) {}
    suspend fun narrowing(start: Int, end: Int, sum: Long) {}
    suspend fun shifting(start: Int, end: Int, sum: Long) {}
    suspend fun finished(start: Int, end: Int, sum: Long) {}
}

suspend fun contiguousMinPlusMax(
    data: List<Long>,
    expectedSum: Long,
    logger: ProgressLogger = object : Day09ProgressLogger {},
) = contiguous(data, expectedSum, logger = logger)
    .let { (start, end) -> data.subList(start, end) }
    .minPlusMax()

suspend fun contiguous(
    data: List<Long>,
    expectedSum: Long,
    minSize: Int = 2,
    logger: ProgressLogger = object : Day09ProgressLogger {},
): Pair<Int, Int> {

    logger as Day09ProgressLogger

    var start = 0
    var end = start + minSize
    var sum = data.subList(start, end).sum()
    logger.startingSearch(data, start, end, sum, expectedSum)

    while (start < data.size - minSize) when {
        sum < expectedSum -> sum = (sum + data[end++]).also { logger.expanding(start, end, sum) }
        sum > expectedSum -> sum = (sum - data[start++]).also { logger.narrowing(start, end, sum) }
        end - start < minSize -> sum = (sum - data[start++] + data[end++]).also { logger.shifting(start, end, sum) }
        else -> return (start to end).also { logger.finished(start, end, sum)}
    }

    error("answer not found")
}

fun part1(input: String) = firstInvalid(input.parsedData()).toString()

suspend fun part2(input: String, logger: ProgressLogger = object : Day09ProgressLogger {}) =
    contiguousMinPlusMax(input.parsedData(), firstInvalid(input.parsedData()), logger).toString()

