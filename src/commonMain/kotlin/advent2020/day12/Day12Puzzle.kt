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

fun rotate(dir: Actions, count: Int): Actions {
    return if (count > 0)
        when (dir) {
            N -> rotate(W, count - 90)
            S -> rotate(E, count - 90)
            E -> rotate(N, count - 90)
            W -> rotate(S, count - 90)
            else -> error("dir is $dir")
        } else if (count < 0)
        when (dir) {
            N -> rotate(E, count + 90)
            S -> rotate(W, count + 90)
            E -> rotate(S, count + 90)
            W -> rotate(N, count + 90)
            else -> error("dir is $dir")
        } else dir

}

fun part1(input: String): String {
    val lines = input.trim().lines()

    var dir = E
    var x = 0
    var y = 0
    lines.forEach { l ->
        val a = Actions.valueOf(l.take(1))
        val c = l.drop(1).toInt()

        when {
            a == N -> y -= c
            a == S -> y += c
            a == E -> x += c
            a == W -> x -= c
            a == L && c == 90 || a == R && c == 270 -> dir = when (dir) {
                N -> W
                S -> E
                E -> N
                W -> S
                else -> error("dir is $dir")
            }
            a == R && c == 90 || a == L && c == 270 -> dir = when (dir) {
                N -> E
                S -> W
                E -> S
                W -> N
                else -> error("dir is $dir")
            }
            a == L && c == 180 || a == R && c == 180 -> dir = when (dir) {
                N -> S
                S -> N
                E -> W
                W -> E
                else -> error("dir is $dir")
            }
            a == F -> when (dir) {
                N -> y -= c
                S -> y += c
                E -> x += c
                W -> x -= c
                else -> error("dir is $dir")
            }
            else -> error("invalid $l")
        }

    }
    return (x.absoluteValue + y.absoluteValue).toString()
}

fun part2(input: String): String {
    val lines = input.trim().lines()

    var x = 0
    var y = 0
    var wx = 10
    var wy = -1
    lines.forEach { l ->
        val action = Actions.valueOf(l.take(1))
        val count = l.drop(1).toInt()

        when (action) {
            N -> wy -= count
            S -> wy += count
            E -> wx += count
            W -> wx -= count
            L -> when (count) {
                90 -> (wy to -wx).let { wx = it.first; wy = it.second }
                180 -> (-wx to -wy).let { wx = it.first; wy = it.second }
                270 -> (-wy to wx).let { wx = it.first; wy = it.second }
                else -> error("can't rotate $l")
            }
            R -> when (count) {
                90 -> (-wy to wx).let { wx = it.first; wy = it.second }
                180 -> (-wx to -wy).let { wx = it.first; wy = it.second }
                270 -> (wy to -wx).let { wx = it.first; wy = it.second }
                else -> error("can't rotate $l")
            }
            F -> {
                x += wx * count;
                y += wy * count
            }
        }
//        println("$l: ship to $x,$y ; wp to $wx,$wy")
    }
    return (x.absoluteValue + y.absoluteValue).toString()
}

