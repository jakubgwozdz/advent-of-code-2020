package advent2020.day00

import advent2020.ErrorField
import advent2020.GenericTaskSection
import advent2020.LogField
import advent2020.ProgressField
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.PuzzleTask
import advent2020.ResultField
import advent2020.TaskSection
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.html.TagConsumer
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement

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
    progressField: ProgressField,
    val logField: LogField,
    launchButton: HTMLButtonElement,
    cancelButton: HTMLButtonElement
) : GenericTaskSection(title, puzzleContext, task, resultField, errorField, progressField, launchButton, cancelButton), Day00Part1ProgressReporter {

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        logField.clear()
    }

    override suspend fun progress(no: Int, total: Int, mass: Long, fuel: Long, sum: Long) {
        progressField.value(no.toDouble(), total.toDouble())
//        console.log("$no/$total:  mass=$mass => fuel=$fuel, sum=$sum")
        logField.addLines("$no/$total:  mass=$mass => fuel=$fuel, sum=$sum")
    }

//    override val delay: Long
//        get() = 13

}

class Day00Part1SectionBuilder : TaskSectionBuilder() {

    init {
        title = "Part 1"
        puzzleContext = day00puzzleContext
        task = ::part1
    }

    lateinit var log: LogField

    override fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {
        log = div.createLogField()
    }

    override fun constructObject(): TaskSection {
        return Day00Part1Section(
            title,
            puzzleContext,
            task,
            resultField,
            errorField,
            progressField,
            log,
            launchButton,
            cancelButton
        )
    }
}
