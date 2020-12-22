package advent2020.day09

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.ResultField
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.myScrollIntoView
import advent2020.readResourceInCurrentPackage
import advent2020.simply
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.TagConsumer
import kotlinx.html.code
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.span
import kotlinx.html.js.style
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import org.w3c.dom.css.CSSStyleRule
import org.w3c.dom.css.CSSStyleSheet

val day09puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day09puzzleInfo = PuzzleInfo("day09", "Encoding Error", 9, 2020, animation = true)

@JsExport
fun createUI() {

    createHeader(day09puzzleInfo, day09puzzleContext)

    //language=CSS
    val style = """
        .entries {
            line-height: 2;
        }
        .entries span {
            padding: 5px;
        }
        .entries span code {
            background-color: initial;
        }
        
        .inside-range {
        }
    """.trimIndent()

    document.body!!.append {
        style { unsafe { raw(style) } }
    }

    taskSection {
        title = "Part 1"
        puzzleContext = day09puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    day09part2Section {
        title = "Part 2"
        puzzleContext = day09puzzleContext
        task = simply(::part2)
        delay = true
    }.buildInBody(document.body!!)

}

class Day09Part2Section(
    genericElements: GenericTaskSectionElements,
    val divElem: HTMLDivElement,
    val currentRangeField: ResultField,
    val currentSumField: ResultField,
    val expectedSumField: ResultField,
) :
    GenericTaskSection(genericElements), Day09ProgressLogger {

    val spans = mutableListOf<HTMLElement>()

    val cssStyleRule = document.styleSheets
        .asList()
        .filterIsInstance<CSSStyleSheet>()
        .last()
        .let { it.cssRules.asList() }
        .filterIsInstance<CSSStyleRule>()
        .single { it.selectorText == ".inside-range" }

    suspend fun setStatus(start: Int, end: Int, sum: Long) {
        cssStyleRule.style.backgroundColor = when {
            sum > expectedSum -> "#583867"
            sum < expectedSum -> "#385367"
            else -> "#386763"
        }

        currentSumField.show("$sum")
        currentRangeField.show("$start .. ${end - 1}")
        if (expectedSum > sum)
            progressField.value(sum, expectedSum)
        else
            progressField.value(expectedSum, sum)
//        progressField.value(1.0 - (1.0 - sum.toDouble()/expectedSum).absoluteValue.pow(0.5), 1.0)
        delayIfChecked(25)

    }

    var expectedSum = 0L
    override suspend fun startingSearch(data: List<Long>, start: Int, end: Int, sum: Long, expectedSum: Long) {
        while (divElem.firstChild != null) {
            divElem.removeChild(divElem.lastChild!!);
        }
        spans.clear()

        data.forEachIndexed { index, entry ->
            spans += divElem.append {
                span {
                    +"$index:"
                    code { +"$entry" }
                    +" "
                }
            }.single()
        }

        this.expectedSum = expectedSum
        expectedSumField.show("$expectedSum")
        (start until end).forEach { spans[it].addClass("inside-range") }
        setStatus(start, end, sum)
    }

    override suspend fun expanding(start: Int, end: Int, sum: Long) {
        console.log("expanding to $start-${end - 1}")
        spans[end - 1].addClass("inside-range")
        spans[end - 1].myScrollIntoView()
        setStatus(start, end, sum)
    }

    override suspend fun narrowing(start: Int, end: Int, sum: Long) {
        console.log("narrowing to $start-${end - 1}")
        spans[start - 1].removeClass("inside-range")
        spans[end - 1].myScrollIntoView()
        spans[start].myScrollIntoView()
        setStatus(start, end, sum)
    }

    override suspend fun shifting(start: Int, end: Int, sum: Long) {
        console.log("shifting to $start-${end - 1}")
        spans[start - 1].removeClass("inside-range")
        spans[end - 1].addClass("inside-range")
        spans[end - 1].myScrollIntoView()
        setStatus(start, end, sum)
    }

    override suspend fun finished(start: Int, end: Int, sum: Long) {
        setStatus(start, end, sum)
        launchButton
            .parentElement!!
            .parentElement!!
            .parentElement!!
            .parentElement!! // lol oh c'mon
            .myScrollIntoView()
    }

}


class Day09Part2SectionBuilder : TaskSectionBuilder() {

    lateinit var divElem: HTMLDivElement

    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) =
        with(bodyBuilder) {
            divElem = div("container entries") { }
        }

    override fun createTaskSpecificLevelFields(bodyBuilder: TagConsumer<HTMLElement>) {
        currentRangeField = bodyBuilder.createResultField("Current Range")
        currentSumField = bodyBuilder.createResultField("Current Sum")
        expectedSumField = bodyBuilder.createResultField("Expected Sum")
    }

    lateinit var currentRangeField: ResultField
    lateinit var currentSumField: ResultField
    lateinit var expectedSumField: ResultField


    override fun constructObject() =
        Day09Part2Section(genericElements(), divElem, currentRangeField, currentSumField, expectedSumField)
}

fun day09part2Section(op: Day09Part2SectionBuilder.() -> Unit) = Day09Part2SectionBuilder().apply(op)
