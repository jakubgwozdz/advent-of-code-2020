package advent2020.day15

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day15puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day15puzzleInfo = PuzzleInfo("day15", "Rambunctious Recitation", 15, 2020)

@JsExport
fun createUI() {

    createHeader(day15puzzleInfo, day15puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day15puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day15puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
