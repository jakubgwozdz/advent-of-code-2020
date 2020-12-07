package advent2020.day02

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.SuspendingWrapper
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.taskSection
import kotlinx.browser.document

val day02puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day02puzzleInfo = PuzzleInfo("day02", "Password Philosophy", 2, 2020)

@JsExport
fun createUI() {

    createHeader(day02puzzleInfo, day02puzzleContext)

    taskSection {
        title = "Part 1"
        puzzleContext = day02puzzleContext
        task = SuspendingWrapper(::part1)::launchWithoutReceiver
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day02puzzleContext
        task = SuspendingWrapper(::part2)::launchWithoutReceiver
    }.buildInBody(document.body!!)

}
