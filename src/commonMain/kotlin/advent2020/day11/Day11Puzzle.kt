package advent2020.day11

import advent2020.utils.atLeast
import kotlin.time.TimeSource.Monotonic

val directions by lazy { (-1..1).flatMap { dr -> (-1..1).map { dc -> (dr to dc) as Pos } } - (0 to 0) }
typealias Vector = Pair<Int, Int>
typealias Pos = Pair<Int, Int>

operator fun Pos.plus(v: Vector): Pos = first + v.first to second + v.second

data class Area(
    private val lines: List<String>,
) {
    constructor(input: String) : this(input.trim().lines())

    val rows: Int = lines.size
    val cols: Int = lines[0].length
    val seats = seats(lines)
    val size = rows * cols
    fun occupied(index: Int) = lines[index / cols][index % cols] == '#'
    operator fun contains(p: Pos) = p.first in lines.indices && p.second in lines[0].indices
}

private fun seats(lines: List<String>) = lines.flatMapIndexed { row, line ->
    line.mapIndexedNotNull { column, c ->
        val pos: Pos = row to column
        when (c) {
            '#' -> pos
            'L' -> pos
            else -> null
        }
    }
}.toHashSet()

fun part1(input: String): String {
    val area = Area(input)
    val seatsWithNeighbours = area.seats.map { pos ->
        val neighbours = directions.mapNotNull { v ->
            val p = pos + v
            if (p in area.seats) return@mapNotNull p else return@mapNotNull null
        }
        pos to neighbours
    }
    return common(area, seatsWithNeighbours, 4).toString()
}

fun part2(input: String): String {
    val area = Area(input)
    val seatsWithNeighbours = area.seats.map { pos ->
        val neighbours = directions.mapNotNull { v ->
            var p = pos + v
            while (p in area) if (p in area.seats) return@mapNotNull p else p += v
            return@mapNotNull null
        }
        pos to neighbours
    }
    return common(area, seatsWithNeighbours, 5).toString()
}

fun common(area: Area, seatsWithNeighbours: List<Pair<Pos, List<Pos>>>, leaveThreshold: Int): Int {
    val s = Monotonic.markNow()
    val cols = area.cols

    val even = BooleanArray(area.rows * cols, area::occupied)
    val odd = BooleanArray(area.rows * cols, area::occupied)

    var count = 0
    println("$count: ${s.elapsedNow()}")

    while (true) {

        val (src, dest) = if (count % 2 == 0) even to odd else odd to even
        val occupied = { pos: Pos -> src[pos.first * cols + pos.second] }
        seatsWithNeighbours.forEach { (pos, list) ->
            dest[pos.first * cols + pos.second] = when (occupied(pos)) {
                true -> !list.atLeast(leaveThreshold, occupied)
                false -> list.none(occupied)
            }
        }

        count++

        println("$count: ${s.elapsedNow()}")
        if (even.contentEquals(odd)) {
            return seatsWithNeighbours.count { (pos, _) -> dest[pos.first * cols + pos.second] }
        }
    }
}
