package advent2020.day11

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day11puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day11puzzleInfo = PuzzleInfo("day11", "Seating System", 11, 2020)

@JsExport
fun createUI() {

    createHeader(day11puzzleInfo, day11puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day11puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day11puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
