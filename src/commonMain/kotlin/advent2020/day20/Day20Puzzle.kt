package advent2020.day20

import advent2020.utils.groups

data class Tile(val id: Long, val lines: List<String>) {
    val top: List<Char> = lines.first().toList()
    val topR: List<Char> = top.reversed()

    val right: List<Char> = lines.map { it.last() }
    val rightR: List<Char> = right.reversed()

    val bottomR: List<Char> = lines.last().toList()
    val bottom: List<Char> = bottomR.reversed()

    val leftR: List<Char> = lines.map { it.first() }
    val left: List<Char> = leftR.reversed()

    fun match(o: Tile): Int {
        return when {
            top == o.bottomR -> 0
            top == o.leftR -> 1
            top == o.topR -> 2
            top == o.rightR -> 3
            right == o.bottomR -> 4
            right == o.leftR -> 5
            right == o.topR -> 6
            right == o.rightR -> 7
            bottom == o.bottomR -> 8
            bottom == o.leftR -> 9
            bottom == o.topR -> 10
            bottom == o.rightR -> 11
            left == o.bottomR -> 12
            left == o.leftR -> 13
            left == o.topR -> 14
            left == o.rightR -> 15
            top == o.bottom -> 0 + 16
            top == o.left -> 1 + 16
            top == o.top -> 2 + 16
            top == o.right -> 3 + 16
            right == o.bottom -> 4 + 16
            right == o.left -> 5 + 16
            right == o.top -> 6 + 16
            right == o.right -> 7 + 16
            bottom == o.bottom -> 8 + 16
            bottom == o.left -> 9 + 16
            bottom == o.top -> 10 + 16
            bottom == o.right -> 11 + 16
            left == o.bottom -> 12 + 16
            left == o.left -> 13 + 16
            left == o.top -> 14 + 16
            left == o.right -> 15 + 16
            else -> -1
        }
    }
}

fun part1(input: String): String {
    val tiles = tiles(input)

    val matches = matches(tiles)

    val result = matches.filterValues { it.size == 2 }
        .keys.fold(1L) { a, i -> a * i }

    println(tiles.size)

    return result.toString()
}

private fun matches(tiles: Map<Long, Tile>) = tiles.map { (id1, t1) ->
    id1 to tiles
        .filter { (id2, _) -> id1 != id2 }
        .map { (id2, t2) -> id2 to t1.match(t2) }
        .filter { (_, m) -> m >= 0 }
        .toMap()
}.toMap()

private fun tiles(input: String) = input.trim().lineSequence()
    .groups { it.isBlank() }
    .map {
        val id = """Tile (\d+):""".toRegex().matchEntire(it[0])?.destructured?.component1()?.toLong()
            ?: error("not id @ ${it[0]}")
        Tile(id, it.drop(1))
    }.toList().associateBy { it.id }

fun part2(input: String): String {
    val lines = input.trim().lines()

    TODO()
}

