package advent2020.day00

import advent2020.ProgressReporter
import advent2020.linesAsFlowOfLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed

interface Day00ProgressReporter : ProgressReporter {
    suspend fun reportPart1Progress(no: Int, total: Int, mass:Long, fuel:Long, sum: Long)
}

suspend fun part1(input: String, reporter: ProgressReporter): String {
    val lines = input.lines()
    var result = 0L

    lines.linesAsFlowOfLong()
        .collectIndexed { index, value ->
            val fuel = fuel(value)
            result += fuel
            if (reporter is Day00ProgressReporter) reporter.reportPart1Progress(index+1, lines.size, value, fuel, result)
            delay(reporter.delay)
        }

    return result.toString()
}

fun fuel(mass: Long): Long {
    return mass / 3 - 2
}

fun fuel2(mass: Long): Long {
    var r = 0L
    var f = fuel(mass)
    while (f > 0) {
        r += f
        f = fuel(f)
    }
    return r
}

