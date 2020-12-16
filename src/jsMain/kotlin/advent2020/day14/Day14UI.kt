package advent2020.day14

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day14puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day14puzzleInfo = PuzzleInfo("day14", "Docking Data", 14, 2020)

@JsExport
fun createUI() {

    createHeader(day14puzzleInfo, day14puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day14puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day14puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
