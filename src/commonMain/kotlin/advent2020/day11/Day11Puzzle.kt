package advent2020.day11

import advent2020.utils.atLeast

fun part1(input: String) = common(input, Grid::adjacent, 4)
fun part2(input: String) = common(input, Grid::visible, 5)

val directions = (-1..1).flatMap { dr -> (-1..1).map { dc -> dr to dc } } - (0 to 0)
typealias Grid = List<List<Char>>
typealias Pos = Pair<Int, Int>

fun Grid.adjacent(pos: Pos) = directions.mapNotNull { (dr, dc) ->
    val r = pos.first + dr
    val c = pos.second + dc
    if (contains(r to c) && (this[r to c] == '#' || this[r to c] == 'L')) r to c
    else null
}

fun Grid.visible(pos: Pos) = directions.mapNotNull { (dr, dc) ->
    var r = pos.first + dr
    var c = pos.second + dc
    while (contains(r to c)) {
        if (this[r to c] == '#' || this[r to c] == 'L') return@mapNotNull r to c
        r += dr
        c += dc
    }
    return@mapNotNull null
}

fun Grid.occupied(pos: Pos) = this[pos] == '#'

fun Grid.contains(pos: Pos) = (pos.first in indices) && (pos.second in this[pos.first].indices)
operator fun Grid.get(pos: Pos) = this[pos.first][pos.second]
fun Grid.totalOccupied() = sumBy { line -> line.count { it == '#' } }

fun common(input: String, interesting: Grid.(Pos) -> List<Pos>, leaveThreshold: Int): String {
    var grid = input.trim().lines().map { it.map { c -> c } } // parsing input

    val cache = grid.indices.flatMap { row ->
        grid[row].indices.mapNotNull { column ->
            val pos = row to column
            if (grid.contains(pos)) pos
            else null
        }
    }.associateWith { grid.interesting(it) }

    val f = { pos: Pos -> cache[pos]!! }

    while (true) {
        val newGrid = grid.mapIndexed { row, line ->
            line.mapIndexed { column, place ->
                val pos = row to column
                when {
                    place == '#' && f(pos).atLeast(leaveThreshold, grid::occupied) -> 'L'
                    place == 'L' && f(pos).none(grid::occupied) -> '#'
                    else -> place
                }
            }
        }
        if (grid == newGrid) {
            return grid.totalOccupied().toString()
        } else {
            grid = newGrid
        }
    }
}


