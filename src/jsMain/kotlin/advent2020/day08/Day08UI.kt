package advent2020.day08

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day08puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day08puzzleInfo = PuzzleInfo("day08", "Handheld Halting", 8, 2020)

@JsExport
fun createUI() {

    createHeader(day08puzzleInfo, day08puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day08puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day08puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
