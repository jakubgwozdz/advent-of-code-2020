package advent2020.day10

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day10puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day10puzzleInfo = PuzzleInfo("day10", "<TITLE>", 10, 2020)

@JsExport
fun createUI() {

    createHeader(day10puzzleInfo, day10puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day10puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day10puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
