package advent2020.day01

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.ReportField
import advent2020.SuspendingWrapper
import advent2020.TaskSection
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.html.TagConsumer
import org.w3c.dom.HTMLElement

val day01puzzleContext by lazy { PuzzleContext(day01myPuzzleInput) }
val day01puzzleInfo = PuzzleInfo("day01", "Report Repair", 1, 2020)

@JsExport
fun createUI() {

    createHeader(day01puzzleInfo)
    createInputSectionWithModal(day01puzzleInfo, day01puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day01puzzleContext
        task = SuspendingWrapper(::part1)::launchWithoutReceiver
    }.buildInBody(document.body!!)

    Part2SectionBuilder().buildInBody(document.body!!)
}

internal class Part2Section(
    genericElements: GenericTaskSectionElements,
    val logField: ReportField,
) : GenericTaskSection(genericElements), Day01Part2ProgressReceiver {

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        logField.clear()
    }

    override suspend fun progress(no: Int, total: Int, entry: Int, comparisons: Int) {
        progressField.value(no, total)
        logField.addLines("testing entry no $no/$total:  value=$entry. Comparisons so far: $comparisons")
        delay(delay)
    }

    override suspend fun final(v1: Int, v2: Int, v3: Int, comparisons: Int) {
        logField.addLines("found entries: $v1, $v2, $v3. Total comparisons: $comparisons")
    }

    override val delay: Long
        get() = if (runWithDelay) 60 else 0

}

internal class Part2SectionBuilder : TaskSectionBuilder() {

    init {
        title = "Part 2"
        puzzleContext = day01puzzleContext
        task = ::part2
        delay = false
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
