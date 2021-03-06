package advent2020.day23

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day23puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day23puzzleInfo = PuzzleInfo("day23", "Crab Cups", 23, 2020)

@JsExport
fun createUI() {

    createHeader(day23puzzleInfo, day23puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day23puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day23puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
