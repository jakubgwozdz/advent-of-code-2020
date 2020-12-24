package advent2020.day24

import kotlin.math.max
import kotlin.math.min

enum class Vector(val dx: Int, val dy: Int) {
    E(2, 0),
    W(-2, 0),
    SE(1, 1),
    SW(-1, 1),
    NE(1, -1),
    NW(-1, -1),
}


data class Position(val x: Int, val y: Int) {
    operator fun plus(v: Vector) = Position(x + v.dx, y + v.dy)
    override fun toString() = "($x,$y)"
}


fun part1(input: String): String {

    val tiles = input.trim().lineSequence()
        .map { line -> tile(line) }
        .fold(emptySet<Position>()) { acc, tile -> if (tile in acc) acc - tile else acc + tile }

    return tiles.size.toString()
}

fun part2(input: String): String {
    var tiles = input.trim().lineSequence()
        .map { line -> tile(line) }
        .fold(emptySet<Position>()) { acc, tile -> if (tile in acc) acc - tile else acc + tile }

    var minX = 0
    var maxX = 0
    var minY = 0
    var maxY = 0

    tiles.forEach { (x, y) ->
        minX = min(minX, x)
        maxX = max(maxX, x)
        minY = min(minY, y)
        maxY = max(maxY, y)
    }

    repeat(100) {
        tiles = (minY - 1..maxY + 1)
            .flatMap { y ->
                val range = if ((y % 2 == 0) == (minX % 2 == 0)) {
                    minX - 2..maxX + 2 step 2
                } else {
                    minX - 1..maxX + 2 step 2
                }
                range.map { x -> Position(x, y) }
            }
            .filter { tile ->
                val adjacent = Vector.values().filter { s -> tile + s in tiles }.size
                if (tile in tiles) adjacent == 1 || adjacent == 2 else adjacent == 2
            }
            .onEach { (x, y) ->
                minX = min(minX, x)
                maxX = max(maxX, x)
                minY = min(minY, y)
                maxY = max(maxY, y)
            }
            .toSet()

    }
    println(" Day 100: ${tiles.size} ")

    return tiles.size.toString()
}


private fun tile(line: String) = sequence<String> {
    var p = 0
    while (p < line.length) {
        when (line[p]) {
            'e', 'w' -> yield(line.substring(p, p + 1)).also { p += 1 }
            's', 'n' -> yield(line.substring(p, p + 2)).also { p += 2 }
            else -> error("unknown `${line.drop(p)}` @ $p")
        }
    }
}
//            .onEach { print("$it ") }
    .map {
        when (it) {
            "e" -> Vector.E
            "w" -> Vector.W
            "se" -> Vector.SE
            "sw" -> Vector.SW
            "ne" -> Vector.NE
            "nw" -> Vector.NW
            else -> error("unknown `$it`")
        }

    }
    .fold(Position(0, 0)) { p, v -> p + v }
//            .also { println(it) }

