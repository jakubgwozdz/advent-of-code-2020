package advent2020.day00

import advent2020.ErrorField
import advent2020.GenericTaskSection
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.PuzzleTask
import advent2020.ResultField
import advent2020.TaskSection
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import advent2020.index
import advent2020.taskSection
import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLProgressElement

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


class Day00Part1Section(
    title: String,
    puzzleContext: PuzzleContext,
    task: PuzzleTask = { _, _ -> TODO(title) },
    resultField: ResultField,
    errorField: ErrorField,
    progressBar: HTMLProgressElement,
    launchButton: HTMLButtonElement,
    cancelButton: HTMLButtonElement
) : GenericTaskSection(title, puzzleContext, task, resultField, errorField, progressBar, launchButton, cancelButton), Day00Part1ProgressReporter {

    val i = index++

    override suspend fun progress(no: Int, total: Int, mass: Long, fuel: Long, sum: Long) {
        progressBar.apply { value = no.toDouble(); max = total.toDouble() }
        console.log("$no/$total:  mass=$mass => fuel=$fuel, sum=$sum")
    }

    override val delay: Long
        get() = 13

}

class Day00Part1SectionBuilder : TaskSectionBuilder() {

    init {
        title = "Part 1"
        puzzleContext = day00puzzleContext
        task = ::part1
    }

    override fun constructObject(): TaskSection {
        return Day00Part1Section(
            title,
            puzzleContext,
            task,
            resultField,
            errorField,
            progressBar,
            launchButton,
            cancelButton
        )
    }
}
