package advent2020.day24

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day24puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day24puzzleInfo = PuzzleInfo("day24", "<TITLE>", 24, 2020, readOnly = true)

@JsExport
fun createUI() {

    createHeader(day24puzzleInfo, day24puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day24puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day24puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
