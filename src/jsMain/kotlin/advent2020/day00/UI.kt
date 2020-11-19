package advent2020.day00

import advent2020.*
import kotlinx.browser.document

val day00puzzleContext by lazy { PuzzleContext(day00myPuzzleInput) }
val day00puzzleInfo = PuzzleInfo("day00", "The Tyranny of the Rocket Equation (from 2019)", 1, 2019)

@JsExport
fun createUI() {

    createHeader(day00puzzleInfo)
    createInputSectionWithModal(day00puzzleInfo, day00puzzleContext)

    Day00Part1SectionBuilder().buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day00puzzleContext
    }.buildInBody(document.body!!)
}


class Day00Part1Section(val delegated: TaskSection) : TaskSection by delegated, Day00Part1ProgressReporter {
    val i = index++

    override suspend fun progress(no: Int, total: Int, mass: Long, fuel: Long, sum: Long) {
        progressBar.apply { value = no.toDouble(); max = total.toDouble() }
        console.log("$no/$total:  mass=$mass => fuel=$fuel, sum=$sum")
    }


    override val delay: Long
        get() = 13

    val taskLauncher = BackgroundTaskLauncher()

    // need to use own impl, not the delegated one
    override fun launch() {
        taskLauncher.launch(this, puzzleContext, task)
    }
    override fun cancel() {
        taskLauncher.cancel(this, puzzleContext, task)
    }


}

class Day00Part1SectionBuilder : TaskSectionBuilder() {

    init {
        title = "Part 1"
        puzzleContext = day00puzzleContext
        task = ::part1
    }

    override fun constructObject(): TaskSection {
        return Day00Part1Section(super.constructObject())
    }
}
