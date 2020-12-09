package advent2020.day09

internal fun String.parsedData() = trim().lines().map(String::toLong)

fun valid(sortedPreamble: List<Long>, expectedSum: Long): Boolean {
    var startIndex = 0
    var endIndex = sortedPreamble.lastIndex

    while (startIndex < endIndex) {
        val sum = sortedPreamble[startIndex] + sortedPreamble[endIndex]
        when {
            sum == expectedSum -> return true
            sum < expectedSum -> startIndex++
            sum > expectedSum -> endIndex--
            else -> error("wtf")
        }
    }

    return false
}

fun firstInvalid(data: List<Long>, preambleLength: Int): Long {

    (preambleLength..data.lastIndex).forEach { index ->
        val preamble = data.subList(index - preambleLength, index).sorted()
        val entry = data[index]
        if (!valid(preamble, entry)) return entry
    }

    error("invalid entry not found")
}

fun contiguous(data: List<Long>, expectedSum: Long): Long {
    (0..data.lastIndex - 2).forEach { start ->
        var min = data[start]
        var max = data[start]
        var sum = data[start]
        var end = start + 1

        while (sum < expectedSum || end < start + 2) {
            sum += data[end]
            min = min.coerceAtMost(data[end])
            max = max.coerceAtLeast(data[end])
            end++
        }

        if (sum == expectedSum) return min + max
    }
    error("answer not found")
}

fun part1(input: String) = firstInvalid(input.parsedData(), 25).toString()
fun part2(input: String) = contiguous(input.parsedData(), firstInvalid(input.parsedData(), 25)).toString()

