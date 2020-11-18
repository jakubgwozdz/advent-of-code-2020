package advent2020.day00

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.TaskSection
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import kotlinx.browser.document

val puzzleContext by lazy { PuzzleContext(myPuzzleInput) }
val puzzleInfo = PuzzleInfo("day00", "The Tyranny of the Rocket Equation (from 2019)", 1, 2019)

@JsExport
fun createUI() {

    createHeader(puzzleInfo)
    createInputSectionWithModal(puzzleInfo, puzzleContext)
    Day00Part1Section().appendTo(document.body!!)
    TaskSection("Part 2", puzzleContext).appendTo(document.body!!)
}


class Day00Part1Section : TaskSection("Part 1", puzzleContext, ::part1), Day00Part1ProgressReporter {

    override suspend fun progress(no: Int, total: Int, mass: Long, fuel: Long, sum: Long) {
        progressBar.apply { value = no.toDouble(); max = total.toDouble() }
        console.log("$no/$total: mass=$mass => fuel=$fuel, sum=$sum")
    }


    override val delay: Long
        get() = 13

}
