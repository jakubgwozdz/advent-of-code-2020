package advent2020.day16

data class Field(val name: String, val range1: IntRange, val range2: IntRange) {
    operator fun contains(v: Int) = v in range1 || v in range2
}

fun part1(input: String): String {
    val lines = input.trim().lines()

    val fieldRegex = """(.+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
    val fields = lines.asSequence().takeWhile { it.isNotBlank() }
        .map { fieldRegex.matchEntire(it)?.destructured ?: error("invalid line `$it`") }
        .map { (name, start1, end1, start2, end2) ->
            Field(name,
                start1.toInt()..end1.toInt(),
                start2.toInt()..end2.toInt())
        }
//        .onEach { println(it) }
        .toList()

    lines.drop(fields.size + 1).first().let { check(it == "your ticket:") { println("invalid `$it`") } }
    val myTicket = lines.drop(fields.size + 2).first().split(',').map { it.toInt() }.also { println("my Ticket: $it") }

    lines.drop(fields.size + 4).first().let { check(it == "nearby tickets:") { println("invalid `$it`") } }
    val result = lines.drop(fields.size + 5).asSequence().flatMap { nearby ->
        val invalid = nearby.split(',').map { it.toInt() }.filterNot { v -> fields.any { v in it } }
//        println("ticket '$nearby` -> invalid $invalid")
        invalid.asSequence()
    }.sum()

    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lines()

    TODO()
}

