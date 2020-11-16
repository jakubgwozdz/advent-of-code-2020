package advent2020

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface ProgressReporter {
    suspend fun startingPhase1() {}
    suspend fun startingPhase2() {}
    suspend fun phaseFinished(result: String) {
        println(result)
    }

    suspend fun error(message: Any?) {
        println("ERROR: $message")
    }
}

val trivialReporter = object : ProgressReporter {}
val emptyReporter = object : ProgressReporter {
    override suspend fun error(message: Any?) {}
    override suspend fun phaseFinished(result: String) {}
}

class PuzzleContext(
    val year: Int = 2020,
    val day: Int,
    var input: String,
    val part1: suspend (String, ProgressReporter) -> String = TODO("Part 1 not yet implemented"),
    val part2: suspend (String, ProgressReporter) -> String = TODO("Part 2 not yet implemented"),
) {

    fun launchPart1(progressReporter: ProgressReporter = trivialReporter) = GlobalScope.launch {
        progressReporter.startingPhase1()
        try {
            val result = part1(input, progressReporter)
            progressReporter.phaseFinished(result)
        } catch (e: Exception) {
            progressReporter.error(e)
        }
    }

    fun launchPart2(progressReporter: ProgressReporter = trivialReporter) = GlobalScope.launch {
        progressReporter.startingPhase2()
        try {
            val result = part2(input, progressReporter)
            progressReporter.phaseFinished(result)
        } catch (e: Exception) {
            progressReporter.error(e)
        }
    }

}

suspend fun List<String>.linesAsFlowOfInt() = asSequence().asFlow().map { it.toInt() }
