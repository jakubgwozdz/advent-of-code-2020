package advent2020.day21

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day21puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day21puzzleInfo = PuzzleInfo("day21", "Allergen Assessment", 21, 2020)

@JsExport
fun createUI() {

    createHeader(day21puzzleInfo, day21puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day21puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day21puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
