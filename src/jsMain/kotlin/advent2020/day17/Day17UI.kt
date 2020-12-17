package advent2020.day17

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day17puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day17puzzleInfo = PuzzleInfo("day17", "<TITLE>", 17, 2020)

@JsExport
fun createUI() {

    createHeader(day17puzzleInfo, day17puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day17puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day17puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
