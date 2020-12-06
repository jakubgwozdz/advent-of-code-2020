package advent2020.day06

import advent2020.*
import kotlinx.browser.document

val day06puzzleContext by lazy { PuzzleContext(day06myPuzzleInput) }
val day06puzzleInfo = PuzzleInfo("day06", "Custom Customs", 6, 2020)

@JsExport
fun createUI() {

    createHeader(day06puzzleInfo, day06puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day06puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day06puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
