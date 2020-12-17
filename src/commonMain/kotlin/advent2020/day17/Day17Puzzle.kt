package advent2020.day17

private fun List<Int>.move(v: List<Int>) = mapIndexed { index, i -> i + v[index] }

fun part1(input: String): String {
    var pocket = input.trim().lines()
        .flatMapIndexed { y, s ->
            s.mapIndexedNotNull { x, c -> if (c == '#') listOf(x, y, 0) else null }
        }
        .toSet()

    val neighbours = neighbours(3).filterNot { it.all { v -> v == 0 } }

    repeat(6) { pocket = cycle(pocket, neighbours) }

    return pocket.size.toString()

}

fun part2(input: String): String {
    var pocket = input.trim().lines()
        .flatMapIndexed { y, s ->
            s.mapIndexedNotNull { x, c -> if (c == '#') listOf(x, y, 0, 0) else null }
        }
        .toSet()

    val neighbours = neighbours(4).filterNot { it.all { v -> v == 0 } }

    repeat(6) { pocket = cycle(pocket, neighbours) }

    return pocket.size.toString()

}

private fun neighbours(d: Int): List<List<Int>> = when {
    d < 0 -> error("d is $d")
    d == 0 -> listOf(emptyList())
    else -> neighbours(d - 1).flatMap { l -> (-1..1).map { l + it } }
}

private fun cycle(
    pocket: Set<List<Int>>,
    neighbours: Iterable<List<Int>>,
): Set<List<Int>> {
    val counts = mutableMapOf<List<Int>, Int>()
    pocket.forEach { p ->
        neighbours.map { p.move(it) }.forEach { counts[it] = (counts[it] ?: 0) + 1 }
    }

    return counts.mapNotNull { (pos, count) ->
        val prevState = pos in pocket
        val nextState = if (prevState) count == 2 || count == 3 else count == 3
        if (nextState) pos else null
    }.toSet()
}

