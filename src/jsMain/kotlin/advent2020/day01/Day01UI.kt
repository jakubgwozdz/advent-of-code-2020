package advent2020.day01

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

val day01puzzleContext by lazy { PuzzleContext(day01myPuzzleInput) }
val day01puzzleInfo = PuzzleInfo("day01", "Report Repair", 1, 2020)

@JsExport
fun createUI() {

    createHeader(day01puzzleInfo)
    createInputSectionWithModal(day01puzzleInfo, day01puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day01puzzleContext
        task = ::part1
    }.buildInBody(document.body!!)

    Part2SectionBuilder().buildInBody(document.body!!)
}

internal class Part2Section(
    genericElements: GenericTaskSectionElements,
    val logField: ReportField,
) : GenericTaskSection(genericElements),
    Day01Part2ProgressReporter {

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        logField.clear()
    }

    override suspend fun progress(no: Int, total: Int, entry: Int, match: Boolean) {
        progressField.value(no.toDouble(), total.toDouble())
        logField.addLines("tested entry no $no/$total:  value=$entry, match=$match")
    }

    override val delay: Long
        get() = 1

}

internal class Part2SectionBuilder : TaskSectionBuilder() {

    init {
        title = "Part 2"
        puzzleContext = day01puzzleContext
        task = ::part2
    }

    lateinit var log: ReportField

    override fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {
        log = div.createLogField()
    }

    override fun constructObject(): TaskSection {
        return Part2Section(
            genericElements(),
            log,
        )
    }
}
