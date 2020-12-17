package advent2020.day17

private fun List<Int>.move(v: List<Int>) = mapIndexed { index, i -> i + v[index] }

fun part1(input: String): String {
    var pocket = input.trim().lines()
        .flatMapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') listOf(x, y) else null } }
        .map { it + 0 } // z-dimension
        .toSet()

    val neighbours = (-1..1).flatMap { dx ->
        (-1..1).flatMap { dy ->
            (-1..1).map { dz -> listOf(dx, dy, dz) }
        }
    }
        .filterNot { it == listOf(0, 0, 0) }

    repeat(6) { pocket = cycle(pocket, neighbours) }

    return pocket.size.toString()

}

fun part2(input: String): String {
    var pocket = input.trim().lines()
        .flatMapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') listOf(x, y) else null } }
        .map { it + 0 } // z-dimension
        .map { it + 0 } // w-dimension
        .toSet()

    val neighbours = (-1..1).flatMap { dx ->
        (-1..1).flatMap { dy ->
            (-1..1).flatMap { dz ->
                (-1..1).map { dw -> listOf(dx, dy, dz, dw) }
            }
        }
    }
        .filterNot { it == listOf(0, 0, 0, 0) }

    repeat(6) { pocket = cycle(pocket, neighbours) }

    return pocket.size.toString()

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

