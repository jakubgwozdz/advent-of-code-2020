package advent2020.day20

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.js.canvas
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

val day20puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day20puzzleInfo = PuzzleInfo("day20", "Jurassic Jigsaw", 20, 2020)

@JsExport
fun createUI() {

    createHeader(day20puzzleInfo, day20puzzleContext)

    day20part1Section {
        title = "Part 1"
        puzzleContext = day20puzzleContext
        task = ::part1
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day20puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}

class Day20Part1Section(genericElements: GenericTaskSectionElements, val canvas: HTMLCanvasElement) :
    GenericTaskSection(genericElements), Day20ProgressLogger {

}

class Day20Part1SectionBuilder : TaskSectionBuilder() {
    lateinit var canvas: HTMLCanvasElement
    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) {
        with(bodyBuilder) {
            canvas = canvas {
                width = "2400"
                height = "2400"
            }
        }
    }

    override fun constructObject() = Day20Part1Section(genericElements(), canvas)
}

fun day20part1Section(op: Day20Part1SectionBuilder.() -> Unit) = Day20Part1SectionBuilder().apply(op)
