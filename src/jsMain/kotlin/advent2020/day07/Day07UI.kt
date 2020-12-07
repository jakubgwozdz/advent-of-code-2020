package advent2020.day07

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResource
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document

val day07puzzleContext by lazy { PuzzleContext(readResource("day07")) }
val day07puzzleInfo = PuzzleInfo("day07", "Handy Haversacks", 7, 2020)

@JsExport
fun createUI() {

    createHeader(day07puzzleInfo, day07puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        subtitle = "Different bags that CAN eventually contain a 'shiny gold' one"
        puzzleContext = day07puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        subtitle = "Total bags inside a 'shiny gold' one"
        puzzleContext = day07puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}
