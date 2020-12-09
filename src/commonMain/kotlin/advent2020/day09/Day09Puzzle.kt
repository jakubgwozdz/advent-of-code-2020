package advent2020.day09

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

fun List<Long>.minPlusMax(): Long {
    var minimum = this[0]
    var maximum = this[0]
    forEach {
        if (it < minimum) minimum = it
        if (it > maximum) maximum = it
    }
    return minimum + maximum
}

fun contiguousMaxMin(data: List<Long>, expectedSum: Long) = contiguous(data, expectedSum)
    .let { (start, end) -> data.subList(start, end) }
    .minPlusMax()

fun contiguous(data: List<Long>, expectedSum: Long, minSize: Int = 2): Pair<Int, Int> {

    var start = 0
    var end = start + minSize
    var sum = data.subList(start, end).sum()

    while (start < data.size - minSize) when {
        sum < expectedSum -> sum += data[end++]
        sum > expectedSum -> sum = sum - data[start++] - data[--end]
        end - start < minSize -> sum = sum - data[start++] + data[end++]
        else -> return start to end
    }

    error("answer not found")
}

fun part1(input: String) = firstInvalid(input.parsedData(), 25).toString()
fun part2(input: String) = contiguousMaxMin(input.parsedData(), firstInvalid(input.parsedData(), 25)).toString()

