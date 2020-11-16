package advent2020.day01

import advent2020.PuzzleContext
import advent2020.createHeader

val puzzleContext by lazy { PuzzleContext(2020, 1, myPuzzleInput, ::part1) }

@JsExport
fun createUI() {
    createHeader(1, puzzleContext)
}
