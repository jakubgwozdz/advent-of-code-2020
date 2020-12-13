package advent2020.day13

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day13puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day13puzzleInfo = PuzzleInfo("day13", "<TITLE>", 13, 2020)

@JsExport
fun createUI() {

    createHeader(day13puzzleInfo, day13puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day13puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day13puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
