package advent2020.day18

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day18puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day18puzzleInfo = PuzzleInfo("day18", "<TITLE>", 18, 2020)

@JsExport
fun createUI() {

    createHeader(day18puzzleInfo, day18puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day18puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day18puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
