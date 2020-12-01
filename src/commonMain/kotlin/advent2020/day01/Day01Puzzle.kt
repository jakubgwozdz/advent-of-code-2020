package advent2020.day01

import advent2020.ProgressReceiver
import advent2020.emptyReceiver
import kotlinx.coroutines.delay

interface Day01Part2ProgressReceiver : ProgressReceiver {
    var comparisons: Int
    suspend fun progress(no: Int, total: Int, entry: Int, match: Boolean)
}

suspend fun part1(input: String, receiver: ProgressReceiver): String {
    val lines = input.trim().lines()
    val numbers = lines.map { it.toInt() }.toSet()
    val result = findEntries(numbers, 2020, emptySet())
    return result.toString()
}

private fun findEntries(
    entries: Set<Int>,
    sum: Int,
    excluded: Set<Int>,
    receiver: ProgressReceiver = emptyReceiver
): Int? {
    val found = entries.firstOrNull {
        it !in excluded && ((sum - it) in entries).also { if (receiver is Day01Part2ProgressReceiver) receiver.comparisons++ }
    } ?: return null
    val result = found * (sum - found) * excluded.fold(1) { a, b -> a * b }
    return result
}

suspend fun part2(input: String, receiver: ProgressReceiver): String {
    val lines = input.trim().lines()
    val numbers = lines.map { it.toInt() }.toSet()
    var i = 1
    val v1 = numbers.first {
        (findEntries(numbers, 2020 - it, setOf(it), receiver) != null)
            // reporting
            .also { match ->
                if (receiver is Day01Part2ProgressReceiver) {
                    receiver.progress(i, numbers.size, it, match)
                    delay(receiver.delay)
                    i++
                }
            }
    }
    val result = findEntries(numbers, 2020 - v1, setOf(v1))!!
    return result.toString()
}

