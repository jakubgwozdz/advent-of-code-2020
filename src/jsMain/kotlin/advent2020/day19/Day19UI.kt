package advent2020.day19

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day19puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day19puzzleInfo = PuzzleInfo("day19", "Monster Messages", 19, 2020)

@JsExport
fun createUI() {

    createHeader(day19puzzleInfo, day19puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day19puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day19puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
