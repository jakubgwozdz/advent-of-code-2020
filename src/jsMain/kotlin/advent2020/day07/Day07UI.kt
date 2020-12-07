package advent2020.day07

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import kotlinx.browser.document

val day07puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day07puzzleInfo = PuzzleInfo("day07", "Handy Haversacks (Visual)", 7, 2020)

@JsExport
fun createUI() {

    createHeader(day07puzzleInfo, day07puzzleContext, readOnly = true)

    day07part1Section {
        title = "Part 1"
        subtitle = "Different bags that CAN eventually contain a 'shiny gold' one"
        puzzleContext = day07puzzleContext
        task = ::part1
        delay = true
    }.buildInBody(document.body!!)

    day07part2Section {
        title = "Part 2"
        subtitle = "Total bags inside a 'shiny gold' one"
        puzzleContext = day07puzzleContext
        task = ::part2
        delay = true
    }.buildInBody(document.body!!)

}

private val bagIdMap = mutableMapOf(rootBag to 0)
private var lastId = 0

internal fun bagId(bag: String) = bagIdMap.getOrPut(bag) { ++lastId}
