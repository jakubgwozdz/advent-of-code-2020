package advent2020.day12

import advent2020.day12.Actions.E
import advent2020.day12.Actions.F
import advent2020.day12.Actions.L
import advent2020.day12.Actions.N
import advent2020.day12.Actions.R
import advent2020.day12.Actions.S
import advent2020.day12.Actions.W
import kotlin.math.absoluteValue

enum class Actions { N, S, E, W, L, R, F }

internal operator fun Pair<Int,Int>.times(count:Int) = first * count to second * count
internal operator fun Pair<Int,Int>.plus(vector: Pair<Int, Int>) = first + vector.first to second + vector.second
internal operator fun Pair<Int,Int>.unaryMinus() = -first to -second

internal fun Pair<Int, Int>.rotateLeft(count: Int) = when (count % 360) {
    0 -> this
    90 -> -second to first
    180 -> -this
    270 -> second to -first
    else -> error("can't rotate $count")
}

internal fun Pair<Int, Int>.rotateRight(count: Int) = when (count % 360) {
    0 -> this
    90 -> second to -first
    180 -> -this
    270 -> -second to first
    else -> error("can't rotate $count")
}

internal fun parseAsSequence(input: String) =
    input.trim().lineSequence().map { Actions.valueOf(it.take(1)) to it.drop(1).toInt() }


val directions = mapOf(N to (0 to 1), S to (0 to -1), E to (1 to 0), W to (-1 to 0))

fun part1(input: String): String {

    var pos = 0 to 0
    var vector = 1 to 0

    parseAsSequence(input).forEach { (action, count) ->
        when (action) {
            N, S, E, W -> pos += directions[action]!! * count
            L -> vector = vector.rotateLeft(count)
            R -> vector = vector.rotateRight(count)
            F -> pos += vector * count
        }
//        println("$l: ship at $pos ; v = $vector")

    }
    return (pos.first.absoluteValue + pos.second.absoluteValue).toString()
}

fun part2(input: String): String {

    var pos = 0 to 0
    var vector = 10 to 1

    parseAsSequence(input).forEach { (action, count) ->

        when (action) {
            N, S, E, W -> vector += directions[action]!! * count
            L -> vector = vector.rotateLeft(count)
            R -> vector = vector.rotateRight(count)
            F -> pos += vector * count
        }
//        println("$l: ship at $pos ; v = $vector")

    }
    return (pos.first.absoluteValue + pos.second.absoluteValue).toString()
}


