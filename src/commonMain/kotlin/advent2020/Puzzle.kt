package advent2020

interface ProgressReporter

class PuzzleContext(
    var input: String,
    val part1progressReporter: ProgressReporter,
    val part2progressReporter: ProgressReporter,
)
