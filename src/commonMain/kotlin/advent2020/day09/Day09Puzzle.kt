package advent2020.day09

import kotlin.math.max
import kotlin.math.min

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

fun firstInvalid(data: List<Long>, preambleLength: Int): Long {

    (preambleLength..data.lastIndex).forEach { index ->
        val preamble = data.subList(index - preambleLength, index)
        val entry = data[index]
        if (!valid(preamble, entry)) return entry
    }

    error("invalid entry not found")
}

fun contiguous(data: List<Long>, expectedSum: Long): Long {
    (0..data.size - 2).forEach { start ->
        var minimum = data[start]
        var maximum = data[start]
        var sum = data[start]
        var end = start + 1

        while (sum < expectedSum || end < start + 2) {
            sum += data[end]
            minimum = min(minimum, data[end])
            maximum = max(maximum, data[end])
            end++
        }

        if (sum == expectedSum) return minimum + maximum
    }
    error("answer not found")
}

fun part1(input: String) = firstInvalid(input.parsedData(), 25).toString()
fun part2(input: String) = contiguous(input.parsedData(), firstInvalid(input.parsedData(), 25)).toString()

