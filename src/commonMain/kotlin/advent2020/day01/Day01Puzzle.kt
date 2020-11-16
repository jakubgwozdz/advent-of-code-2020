package advent2020.day01

import advent2020.ProgressReporter
import advent2020.linesAsFlowOfInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed

interface Day01ProgressReporter : ProgressReporter {
    suspend fun reportPart1Progress(no: Int, total: Int, mass:Int, fuel:Int, sum: Int)
}

suspend fun part1(input: String, reporter: ProgressReporter): String {
    val lines = input.lines()
    var result = 0

    lines.linesAsFlowOfInt()
        .collectIndexed { index, value ->
            val fuel = fuel(value)
            result += fuel
            if (reporter is Day01ProgressReporter) reporter.reportPart1Progress(index+1, lines.size, value, fuel, result)
            if (reporter.delay > 0) delay(reporter.delay)
//            var i = 0
//            while (i < 60000000) i++
        }

    return result.toString()
}

fun fuel(mass: Int): Int {
    return mass / 3 - 2
}

fun fuel2(mass: Int): Int {
    var r = 0
    var f = fuel(mass)
    while (f > 0) {
        r += f
        f = fuel(f)
    }
    return r
}

