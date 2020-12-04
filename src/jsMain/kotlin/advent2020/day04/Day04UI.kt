package advent2020.day04

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.ReportField
import advent2020.ResultField
import advent2020.TaskSection
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import kotlinx.browser.document
import kotlinx.html.TagConsumer
import org.w3c.dom.HTMLElement

val day04puzzleContext by lazy { PuzzleContext(day04myPuzzleInput) }
val day04puzzleInfo = PuzzleInfo("day04", "Passport Processing", 4, 2020)

@JsExport
fun createUI() {

    createHeader(day04puzzleInfo)
    createInputSectionWithModal(day04puzzleInfo, day04puzzleContext, readOnly = true)

    day04taskSection {
        title = "Part 1"
        puzzleContext = day04puzzleContext
        task = ::part1
    }.buildInBody(document.body!!)

    day04taskSection {
        title = "Part 2"
        puzzleContext = day04puzzleContext
        task = ::part2
    }.buildInBody(document.body!!)

}

internal class Day04TaskSection(
    genericElements: GenericTaskSectionElements,
    val logField: ReportField,
    val withMissingField: ResultField,
    val withInvalidField: ResultField,
) : GenericTaskSection(genericElements), Day04ProgressReceiver {

    var valid = 0
    var withInvalid = 0
    var withMissing = 0

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        logField.clear()
        logField.addLines("Starting", "")
        valid = 0
        withInvalid = 0
        withMissing = 0
    }

    override suspend fun validPassport(passport: Passport) {
        logField.addLines(*passport.lines.toTypedArray(), "✅ [VALID]", "")
        valid++
        resultField.show(valid.toString())
        delayIfChecked(33)
    }

    override suspend fun invalidPassportMissingFields(passport: Passport, missingFields: Set<PassportField>) {
        logField.addLines(*passport.lines.toTypedArray(), "⛔ [INVALID] - missing fields $missingFields", "")
        withMissing++
        withMissingField.show(withMissing.toString())
        delayIfChecked(100)
    }

    override suspend fun invalidPassportInvalidFields(passport: Passport, invalidFields: Set<PassportFieldValue>) {
        logField.addLines(*passport.lines.toTypedArray(), "⛔ [INVALID] - invalid fields ${invalidFields.map { it.field }}", "")
        withInvalid++
        withInvalidField.show(withInvalid.toString())
        delayIfChecked(100)
    }

    override suspend fun success(result: String) {
        super<GenericTaskSection>.success(result)
        logField.addLines("Found $valid valid passports, $withMissing with missing fields, $withInvalid with invalid fields")
    }

}

internal class Day04TaskSectionBuilder : TaskSectionBuilder() {
    init {
        delay = true
    }

    lateinit var log: ReportField

    override fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {
        log = div.createLogField()
    }

    override fun createTaskSpecificLevelFields(div: TagConsumer<HTMLElement>) {
        withMissingField = div.createResultField("With missing fields")
        withInvalidField = div.createResultField("With invalid fields")
    }

    lateinit var withMissingField: ResultField
    lateinit var withInvalidField: ResultField

    override fun constructObject(): TaskSection {
        return Day04TaskSection(
            genericElements(),
            log,
            withMissingField,
            withInvalidField,
        )
    }

}

internal fun day04taskSection(op: Day04TaskSectionBuilder.() -> Unit) = Day04TaskSectionBuilder().apply(op)
