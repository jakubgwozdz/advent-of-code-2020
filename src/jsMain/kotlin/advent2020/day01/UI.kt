package advent2020.day01

import advent2020.ProgressReporter
import advent2020.createHeader

@JsExport
fun createUI() {
    createHeader(1, puzzleContext)
    puzzleContext.progressReporter = progressReporter
}

val progressReporter = object : ProgressReporter {
    override suspend fun phase1Finished(result: String) {
        TODO("Not yet implemented")
    }

    override suspend fun phase2Finished(result: String) {
        TODO("Not yet implemented")
    }
}
