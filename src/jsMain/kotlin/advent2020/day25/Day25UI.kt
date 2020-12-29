package advent2020.day25

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day25puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day25puzzleInfo = PuzzleInfo("day25", "<TITLE>", 25, 2020, readOnly = true)

@JsExport
fun createUI() {

    createHeader(day25puzzleInfo, day25puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day25puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day25puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
