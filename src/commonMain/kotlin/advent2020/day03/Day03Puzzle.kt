package advent2020.day03

fun part1(input: String): String {
    val lines = input.trim().lines()

    val result = countTrees(lines, 3, 1)

    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lines()

    val moves = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
    val result = moves.map { countTrees(lines, it.first, it.second) }.reduce { a, b -> a * b }

    return result.toString()
}

private fun countTrees(lines: List<String>, x: Int, y: Int): Long = lines
    .filterIndexed { index, line -> index % y == 0 && line[index / y * x % line.length] == '#' }
    .count().toLong()

