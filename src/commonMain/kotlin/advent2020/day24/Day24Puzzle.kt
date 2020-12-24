package advent2020.day24

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

    repeat(100) {

        val neighbours = mutableMapOf<Position, Int>()
        tiles.forEach { tile ->
            Vector.values().forEach { v ->
                neighbours[tile + v] = (neighbours[tile + v] ?: 0) + 1
            }
        }
        tiles = neighbours
            .filter { (tile, count) -> if (tile in tiles) count == 1 || count == 2 else count == 2 }
            .keys

    }
    println(" Day 100: ${tiles.size} ")

    return tiles.size.toString()
}


private fun tile(line: String) =
    sequence {
        var p = 0
        while (p < line.length) {
            when (line[p]) {
                'e', 'w' -> yield(line.substring(p, p + 1)).also { p += 1 }
                's', 'n' -> yield(line.substring(p, p + 2)).also { p += 2 }
                else -> error("unknown `${line.drop(p)}` @ $p")
            }
        }
    }
        .map { Vector.valueOf(it.toUpperCase()) }
        .fold(Position(0, 0)) { p, v -> p + v }

