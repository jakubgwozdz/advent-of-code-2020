package advent2020.day16

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day16puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day16puzzleInfo = PuzzleInfo("day16", "<TITLE>", 16, 2020)

@JsExport
fun createUI() {

    createHeader(day16puzzleInfo, day16puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day16puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day16puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
