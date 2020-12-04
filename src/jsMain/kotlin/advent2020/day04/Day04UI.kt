package advent2020.day04

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day04puzzleContext by lazy { PuzzleContext(day04myPuzzleInput) }
val day04puzzleInfo = PuzzleInfo("day04", "Passport Processing", 4, 2020)

@JsExport
fun createUI() {

    createHeader(day04puzzleInfo)
    createInputSectionWithModal(day04puzzleInfo, day04puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        puzzleContext = day04puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day04puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
