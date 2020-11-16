package advent2020

import kotlinx.coroutines.Job

interface SolverController {
    fun startPhase1(): Job
    fun startPhase2(): Job
}

interface ProgressReporter {
    suspend fun phase1Finished(result: String)
    suspend fun phase2Finished(result: String)
}

val trivialReporter = object : ProgressReporter {
    override suspend fun phase1Finished(result: String) = println(result)
    override suspend fun phase2Finished(result: String) = println(result)
}

class PuzzleContext(
    val year: Int = 2020,
    val day: Int,
    var input: String,
    val solverController: SolverController,
    var progressReporter: ProgressReporter = trivialReporter,
)
