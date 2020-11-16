package advent2020.day01

import advent2020.ProgressReporter
import advent2020.PuzzleContext

const val myPuzzleInput = "1234 1234"

val part1progressReporter = object : ProgressReporter {}
val part2progressReporter = object : ProgressReporter {}

val puzzleContext = PuzzleContext(myPuzzleInput, part1progressReporter, part2progressReporter)
