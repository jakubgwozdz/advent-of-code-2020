package advent2020.day12

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day12puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day12puzzleInfo = PuzzleInfo("day12", "Rain Risk", 12, 2020)

@JsExport
fun createUI() {

    createHeader(day12puzzleInfo, day12puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day12puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day12puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
