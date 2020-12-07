package advent2020

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

typealias PuzzleTask = suspend (String, ProgressLogger) -> String

open class PuzzleContext(
    var input: String,
//    private val part1: PuzzleTask = { _, _ -> TODO("Part 1 not yet implemented") },
//    private val part2: PuzzleTask = { _, _ -> TODO("Part 2 not yet implemented") },
)

interface ProgressLogger {
    suspend fun starting() {}
    suspend fun success(result: String) {}
    suspend fun error(message: Any?) {}
}

val trivialReceiver = object : ProgressLogger {
    override suspend fun error(message: Any?) = println("ERROR: $message")
    override suspend fun success(result: String) = println(result)
}

val emptyReceiver = object : ProgressLogger {
}


class SuspendingWrapper(val task: (String) -> String) {
    suspend fun launchWithoutReceiver(input: String, logger: ProgressLogger) = task(input)
}

fun suspending(task: (String) -> String) = SuspendingWrapper(task)::launchWithoutReceiver

interface TaskLauncher {
    fun start(logger: ProgressLogger, puzzleContext: PuzzleContext, task: PuzzleTask)
    fun cancel(logger: ProgressLogger, puzzleContext: PuzzleContext, task: PuzzleTask)
}

class BackgroundTaskLauncher : TaskLauncher {
    var activeJob: Job? = null
    override fun start(logger: ProgressLogger, puzzleContext: PuzzleContext, task: PuzzleTask) {
        activeJob?.let { if (it.isActive) it.cancel("cancelling because rerun") }
        activeJob = MainScope().launch {
            logger.starting()
            try {
                val result = task(puzzleContext.input, logger)
                logger.success(result)
            } catch (e: Throwable) {
                logger.error(e)
            }
        }
    }

    override fun cancel(logger: ProgressLogger, puzzleContext: PuzzleContext, task: PuzzleTask) {
        activeJob?.let { if (it.isActive) it.cancel("cancelling") }
    }
}
