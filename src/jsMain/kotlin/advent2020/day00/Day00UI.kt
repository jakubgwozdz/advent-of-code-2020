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
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.js.figure
import kotlinx.html.js.pre
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLPreElement
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
    val log: HTMLPreElement,
    launchButton: HTMLButtonElement,
    cancelButton: HTMLButtonElement
) : GenericTaskSection(title, puzzleContext, task, resultField, errorField, progressBar, launchButton, cancelButton), Day00Part1ProgressReporter {

    private val lines = mutableListOf<String>()

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        lines.clear()
        log.textContent = lines.joinToString("\n")
    }

    override suspend fun progress(no: Int, total: Int, mass: Long, fuel: Long, sum: Long) {
        progressBar.apply { value = no.toDouble(); max = total.toDouble() }
//        console.log("$no/$total:  mass=$mass => fuel=$fuel, sum=$sum")
        lines += "$no/$total:  mass=$mass => fuel=$fuel, sum=$sum"
        while (lines.size > 5) lines.removeAt(0)

        log.textContent = lines.joinToString("\n")
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

    lateinit var log: HTMLPreElement

    override fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {
        with(div) {
            figure("box") {
                log = pre { }
            }
        }
    }

    override fun constructObject(): TaskSection {
        return Day00Part1Section(
            title,
            puzzleContext,
            task,
            resultField,
            errorField,
            progressBar,
            log,
            launchButton,
            cancelButton
        )
    }
}
