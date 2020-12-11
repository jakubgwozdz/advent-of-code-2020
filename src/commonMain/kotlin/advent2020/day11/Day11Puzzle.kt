package advent2020.day11

import advent2020.utils.atLeast

fun part1(input: String) = common(input, Grid::adjacent, 4)
fun part2(input: String) = common(input, Grid::visible, 5)

val directions = (-1..1).flatMap { dr -> (-1..1).map { dc -> dr to dc } } - (0 to 0)
typealias Grid = Map<Pos, Boolean>
typealias Pos = Pair<Int, Int>

fun Grid.adjacent(pos: Pos) = directions.mapNotNull { (dr, dc) ->
    val p = pos.first + dr to pos.second + dc
    if (p in this) p
    else null
}

fun Grid.visible(pos: Pos) = directions.mapNotNull { (dr, dc) ->
    var r = pos.first + dr
    var c = pos.second + dc
    while (c in (0..100) && r in (0..100)) { // TODO min - max better
        val p = r to c
        if (p in this) return@mapNotNull p
        r += dr
        c += dc
    }
    return@mapNotNull null
}

fun Grid.occupied(pos: Pos) = this[pos] ?: false


fun common(input: String, interesting: Grid.(Pos) -> List<Pos>, leaveThreshold: Int): String {
    var grid = input.trim().lines()// parsing input
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

    val cache = grid.mapValues { (k,v)->grid.interesting(k) }

//    val even = BooleanArray(10000)
//    val odd = BooleanArray(10000)
//
//    grid.forEach { (k,v)->even }

    var count = 0
    var changes = 0
    while (true) {
        count++
        var changed = false
        val newGrid = grid.mapValues { (pos, occupied) ->
            when {
                occupied && cache[pos]!!.atLeast(leaveThreshold) { grid[it]!! } -> false.also { changes++ ; changed = true }
                !occupied && cache[pos]!!.none { grid[it]!! } -> true.also { changes++ ; changed = true }
                else -> occupied
            }

        }
        if (!changed) {
            println("$count iter, $changes switches, ${grid.size} seats")
            return grid.values.count { it }.toString()
        } else {
            grid = newGrid
        }
    }
}


