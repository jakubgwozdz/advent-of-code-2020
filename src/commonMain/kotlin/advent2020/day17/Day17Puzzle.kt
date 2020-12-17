package advent2020.day17

fun part1(input: String): String = common(input) { x, y -> listOf(x, y, 0) }

fun part2(input: String): String = common(input) { x, y -> listOf(x, y, 0, 0) }

private fun common(input: String, init: (Int, Int) -> List<Int>): String {

    var pocket = input.trim().lines()
        .flatMapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') init(x, y) else null } }
        .toSet()

    val dim = pocket.first().size
    val neighbours = neighbours(dim).filterNot { it.all { v -> v == 0 } }

    repeat(6) { pocket = cycle(pocket, neighbours) }

    return pocket.size.toString()
}

private fun neighbours(dim: Int): List<List<Int>> = when (dim) {
    0 -> listOf(emptyList())
    else -> neighbours(dim - 1).flatMap { l -> (-1..1).map { l + it } }
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

private fun List<Int>.move(v: List<Int>) = mapIndexed { index, i -> i + v[index] }


