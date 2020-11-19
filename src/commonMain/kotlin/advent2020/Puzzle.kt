package advent2020

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

typealias PuzzleTask = suspend (String, ProgressReceiver) -> String

open class PuzzleContext(
    var input: String,
//    private val part1: PuzzleTask = { _, _ -> TODO("Part 1 not yet implemented") },
//    private val part2: PuzzleTask = { _, _ -> TODO("Part 2 not yet implemented") },
) {

}

interface ProgressReceiver {
    suspend fun starting() {}
    suspend fun success(result: String) {}
    suspend fun error(message: Any?) {}

    val delay: Long get() = 0
}

val trivialReceiver = object : ProgressReceiver {
    override suspend fun error(message: Any?) = println("ERROR: $message")
    override suspend fun success(result: String) = println(result)
}

val emptyReceiver = object : ProgressReceiver {
}


suspend fun List<String>.linesAsFlowOfLong() = asSequence().asFlow().map { it.toLong() }

interface TaskLauncher {
    fun launch(receiver: ProgressReceiver, puzzleContext: PuzzleContext, task: PuzzleTask)
    fun cancel(receiver: ProgressReceiver, puzzleContext: PuzzleContext, task: PuzzleTask)
}

class BackgroundTaskLauncher :TaskLauncher{
    var activeJob: Job? = null
    override fun launch(receiver: ProgressReceiver, puzzleContext: PuzzleContext, task: PuzzleTask) {
        activeJob?.let { if (it.isActive) it.cancel("cancelling because rerun") }
        activeJob = GlobalScope.launch {
            receiver.starting()
            try {
                val result = task(puzzleContext.input, receiver)
                receiver.success(result)
            } catch (e: Throwable) {
                receiver.error(e)
            }
        }


    }
    override fun cancel(receiver: ProgressReceiver, puzzleContext: PuzzleContext, task: PuzzleTask) {
        activeJob?.let { if (it.isActive) it.cancel("cancelling") }
    }
}
