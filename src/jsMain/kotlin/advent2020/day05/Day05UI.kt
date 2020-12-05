package advent2020.day05

import advent2020.*
import kotlinx.browser.document

val day05puzzleContext by lazy { PuzzleContext(day05myPuzzleInput) }
val day05puzzleInfo = PuzzleInfo("day05", "Binary Boarding ", 5, 2020)

@JsExport
fun createUI() {

    createHeader(day05puzzleInfo, day05puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1 - highest seatId"
        puzzleContext = day05puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2 - missing seatId"
        puzzleContext = day05puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
