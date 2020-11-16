package advent2020.day01

import advent2020.PuzzleContext
import advent2020.createHeader

val puzzleContext by lazy { PuzzleContext(2020, 1, myPuzzleInput, ::part1) }

@JsExport
fun createUI() {
    createHeader(1, puzzleContext, object:Day01ProgressReporter{
        override suspend fun reportPart1Progress(no: Int, total: Int, mass:Int, fuel:Int, sum: Int) {
            console.log("$no/$total: mass=$mass => fuel=$fuel, sum=$sum")
        }

        override val delay: Long
            get() = 100
    })
}
