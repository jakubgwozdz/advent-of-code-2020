package advent2020.day11

import advent2020.utils.atLeast

fun part1(input: String) = common(input, Graph::adjacent, 4)
fun part2(input: String) = common(input, Graph::visible, 5)

val directions = (-1..1).flatMap { dr -> (-1..1).map { dc -> dr to dc } } - (0 to 0)
typealias Graph = Map<Pos, Boolean>
typealias Pos = Pair<Int, Int>

fun Graph.adjacent(pos: Pos) = directions.mapNotNull { (dr, dc) ->
    val p = pos.first + dr to pos.second + dc
    if (p in this) p
    else null
}

fun Graph.visible(pos: Pos) = directions.mapNotNull { (dr, dc) ->
    var r = pos.first + dr
    var c = pos.second + dc
    while (c in (0..100) && r in (0..100)) {
        val p = r to c
        if (p in this) return@mapNotNull p
        r += dr
        c += dc
    }
    return@mapNotNull null
}

// TODO hardcoded constraint on grid size 100x100.
val Pos.index get() = first * 100 + second

fun common(input: String, interesting: Graph.(Pos) -> List<Pos>, leaveThreshold: Int): String {
    val graph = input.trim().lines()// parsing input
        .flatMapIndexed { row, line ->
            line.mapIndexedNotNull { column, c ->
                val pos: Pos = row to column
                when (c) {
                    '#' -> pos to true
                    'L' -> pos to false
                    else -> null
                }
            }
        }.toMap()

    val cache = graph.mapValues { (k, v) -> graph.interesting(k) }

    val even = BooleanArray(10000)
    val odd = BooleanArray(10000)

    graph.forEach { (k, v) -> even[k.index] = v }

    var count = 0

    while (true) {

        val (src, dest) = if (count % 2 == 0) even to odd else odd to even
        val occupied = { p: Pos -> src[p.index] }

        cache.forEach { (pos, list) ->
            dest[pos.index] = when (occupied(pos)) {
                true -> !list.atLeast(leaveThreshold, occupied)
                false -> list.none(occupied)
            }
        }

        count++

        if (dest.contentEquals(src)) {
            return graph.keys.count { dest[it.index] }.toString()
        }
    }
}


