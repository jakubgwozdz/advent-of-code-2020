package advent2020.day00

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.ReportField
import advent2020.TaskSection
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.html.TagConsumer
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
    genericElements: GenericTaskSectionElements,
    val logField: ReportField,
) : GenericTaskSection(genericElements), Day00Part1ProgressReporter {

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        logField.clear()
    }

    override suspend fun progress(no: Int, total: Int, mass: Long, fuel: Long, sum: Long) {
        progressField.value(no, total)
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

    lateinit var log: ReportField

    override fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {
        log = div.createLogField()
    }

    override fun constructObject(): TaskSection {
        return Day00Part1Section(
            genericElements(),
            log,
        )
    }
}
