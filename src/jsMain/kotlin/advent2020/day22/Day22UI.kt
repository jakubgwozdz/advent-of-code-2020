package advent2020.day22

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day22puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day22puzzleInfo = PuzzleInfo("day22", "Crab Combat", 22, 2020)

@JsExport
fun createUI() {

    createHeader(day22puzzleInfo, day22puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day22puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day22puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
