package advent2020.day03

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.SuspendingWrapper
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day03puzzleContext by lazy { PuzzleContext(day03myPuzzleInput) }
val day03puzzleInfo = PuzzleInfo("day03", "Toboggan Trajectory", 3, 2020)

@JsExport
fun createUI() {

    createHeader(day03puzzleInfo)
    createInputSectionWithModal(day03puzzleInfo, day03puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day03puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day03puzzleContext
        task = suspending (::part2)
    }.buildInBody(document.body!!)

}
