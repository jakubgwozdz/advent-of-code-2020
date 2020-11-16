package advent2020

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

interface ProgressReporter {
    suspend fun startingPhase1() {}
    suspend fun startingPhase2() {}
    suspend fun phaseFinished(result: String) {
        println(result)
    }

    suspend fun error(message: Any?) {
        println("ERROR: $message")
    }

    val delay: Long get() = 0
}

val trivialReporter = object : ProgressReporter {}
val emptyReporter = object : ProgressReporter {
    override suspend fun error(message: Any?) {}
    override suspend fun phaseFinished(result: String) {}
}

open class PuzzleContext(
    val year: Int = 2020,
    val day: Int,
    var input: String,
    private val part1: suspend (String, ProgressReporter) -> String = { _, _ -> TODO("Part 1 not yet implemented") },
    private val part2: suspend (String, ProgressReporter) -> String = { _, _ -> TODO("Part 2 not yet implemented") },
) {

    suspend fun launchPart1(progressReporter: ProgressReporter = trivialReporter) {
        progressReporter.startingPhase1()
        return try {
            val result = part1(input, progressReporter)
            progressReporter.phaseFinished(result)
        } catch (e: Throwable) {
            progressReporter.error(e)
        }
    }

    suspend fun launchPart2(progressReporter: ProgressReporter = trivialReporter) {
        progressReporter.startingPhase2()
        try {
            val result = part2(input, progressReporter)
            progressReporter.phaseFinished(result)
        } catch (e: Throwable) {
            progressReporter.error(e)
        }
    }

}

suspend fun List<String>.linesAsFlowOfInt() = asSequence().asFlow().map { it.toInt() }
