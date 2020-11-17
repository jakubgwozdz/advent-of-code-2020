package advent2020

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

interface ProgressReporter {
    suspend fun startingPart1() {}
    suspend fun startingPart2() {}
    suspend fun phaseFinished(result: String) {}
    suspend fun error(message: Any?) {}

    val delay: Long get() = 0
}

val trivialReporter = object : ProgressReporter {
    override suspend fun error(message: Any?) = println("ERROR: $message")
    override suspend fun phaseFinished(result: String) = println(result)
}

val emptyReporter = object : ProgressReporter {
}

open class PuzzleContext(
    var input: String,
    private val part1: suspend (String, ProgressReporter) -> String = { _, _ -> TODO("Part 1 not yet implemented") },
    private val part2: suspend (String, ProgressReporter) -> String = { _, _ -> TODO("Part 2 not yet implemented") },
) {

    suspend fun launchPart1(progressReporter: ProgressReporter = trivialReporter) {
        progressReporter.startingPart1()
        return try {
            val result = part1(input, progressReporter)
            progressReporter.phaseFinished(result)
        } catch (e: Throwable) {
            progressReporter.error(e)
        }
    }

    suspend fun launchPart2(progressReporter: ProgressReporter = trivialReporter) {
        progressReporter.startingPart2()
        try {
            val result = part2(input, progressReporter)
            progressReporter.phaseFinished(result)
        } catch (e: Throwable) {
            progressReporter.error(e)
        }
    }

}

suspend fun List<String>.linesAsFlowOfLong() = asSequence().asFlow().map { it.toLong() }
