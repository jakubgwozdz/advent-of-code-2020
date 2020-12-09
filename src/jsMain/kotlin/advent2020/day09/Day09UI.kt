package advent2020.day09

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day09puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day09puzzleInfo = PuzzleInfo("day09", "Encoding Error", 9, 2020)

@JsExport
fun createUI() {

    createHeader(day09puzzleInfo, day09puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day09puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day09puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
