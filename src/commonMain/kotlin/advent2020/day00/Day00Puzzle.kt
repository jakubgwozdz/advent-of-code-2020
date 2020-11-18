package advent2020.day00

import advent2020.ProgressReceiver
import advent2020.linesAsFlowOfLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed

interface Day00Part1ProgressReporter : ProgressReceiver {
    suspend fun progress(no: Int, total: Int, mass:Long, fuel:Long, sum: Long)
}

suspend fun part1(input: String, receiver: ProgressReceiver): String {
    val lines = input.lines()
    var result = 0L

    lines.linesAsFlowOfLong()
        .collectIndexed { index, value ->
            val fuel = fuel(value)
            result += fuel
            if (receiver is Day00Part1ProgressReporter) receiver.progress(index+1, lines.size, value, fuel, result)
//            if (index > 80) error("umpf")
            delay(receiver.delay)
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

