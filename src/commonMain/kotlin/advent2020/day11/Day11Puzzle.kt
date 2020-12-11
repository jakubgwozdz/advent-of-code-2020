package advent2020.day11

fun part1(input: String) = common(input, { r, c -> adjacent(r, c) == 0 }, { r, c -> adjacent(r, c) >= 4 })
fun part2(input: String) = common(input, { r, c -> visible(r, c) == 0 }, { r, c -> visible(r, c) >= 5 })

val directions = (-1..1).flatMap { dr -> (-1..1).map { dc -> dr to dc } } - (0 to 0)
typealias Grid = List<List<Char>>

fun Grid.adjacent(row: Int, column: Int) =
    directions.count { (dr, dc) ->
        val r = row + dr
        val c = column + dc
        (r in indices && c in this[r].indices && this[r][c] == '#')
    }

fun Grid.visible(row: Int, column: Int) =
    directions.count { (dr, dc) ->
        var r = row + dr
        var c = column + dc
        while (r in indices && c in this[r].indices) {
            if (this[r][c] == '#') return@count true
            if (this[r][c] == 'L') return@count false
            r += dr
            c += dc
        }
        return@count false
    }

fun Grid.totalOccupied() = sumBy { line -> line.count { it == '#' } }

fun common(input: String, shouldOccupy: Grid.(Int, Int) -> Boolean, shouldLeave: Grid.(Int, Int) -> Boolean): String {
    var grid = input.trim().lines().map { it.map { c -> c } } // parsing input

    while (true) {
        val newGrid = grid.mapIndexed { row, line ->
            line.mapIndexed { column, place ->
                when {
                    place == '#' && shouldLeave(grid, row, column) -> 'L'
                    place == 'L' && shouldOccupy(grid, row, column) -> '#'
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


