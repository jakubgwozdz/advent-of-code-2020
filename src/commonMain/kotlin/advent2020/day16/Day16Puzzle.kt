package advent2020.day16

data class Field(val name: String, val range1: LongRange, val range2: LongRange) {
    operator fun contains(v: Long) = v in range1 || v in range2
}

fun part1(input: String): String {
    val lines = input.trim().lines()

    val fields = fields(lines)

    val result = lines.drop(fields.size + 5).asSequence().flatMap { nearby ->
        nearby.split(',').map { it.toLong() }
            .filterNot { v -> fields.any { v in it } }
            .asSequence()
    }.sum()

    return result.toString()
}

val fieldRegex = """(.+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
private fun fields(lines: List<String>): List<Field> = lines.asSequence()
    .takeWhile { it.isNotBlank() }
    .map { fieldRegex.matchEntire(it)?.destructured ?: error("invalid line `$it`") }
    .map { (name, start1, end1, start2, end2) ->
        Field(name,
            start1.toLong()..end1.toLong(),
            start2.toLong()..end2.toLong())
    }
//        .onEach { println(it) }
    .toList()

fun part2(input: String): String {
    val lines = input.trim().lines()

    val fields = fields(lines)

    val myTicket = lines.drop(fields.size + 2).first().split(',').map { it.toLong() }.also { println("my Ticket: $it") }

    val validTickets = lines.drop(fields.size + 5)
        .map { it.split(',').map(String::toLong) }
        .filter { nearby -> nearby.all { v -> fields.any { v in it } } }

    val fieldIndices = fields.associateWith { fields.indices.toSet() }.toMutableMap()

    validTickets.forEach { ticket ->
        ticket.forEachIndexed { index, v ->
            fields.filterNot { v in it }.forEach { fieldIndices[it] = fieldIndices[it]!! - index }
        }
    }

//    display(fieldIndices)
    var changed = analyze(fieldIndices)
    while (changed) {
//        println()
//        display(fieldIndices)
        val n1 = fieldIndices.toMap()
        changed = analyze(fieldIndices)
        val n2 = fieldIndices.toMap()
        if (n1 == n2 && changed) error("infinite loop")
    }

    return fields.filter { it.name.startsWith("departure") }
        .map { fieldIndices[it]!!.single() }
        .map { myTicket[it].toLong() }
        .fold(1L) { a, b -> a * b }.toString()
}

fun analyze(fieldIndices: MutableMap<Field, Set<Int>>): Boolean {
    var changed = false

    (0 until fieldIndices.size).forEach { index ->
        fieldIndices.entries
            .singleOrNull { (f, v) -> v.size > 1 && index in v }
//            ?.also { (f,_) -> println("`${f.name}` is only one with $index") }
            ?.let { (f, _) ->
                fieldIndices[f] = setOf(index)
                    .also { changed = true }
            }
    }

    return changed
}

fun display(m: Map<Field, Set<Int>>) {
    val keys = m.keys.sortedBy { it.name }
    keys.forEach { f ->
        println("${f.name.padStart(30)}: ${keys.indices.map { i -> if (i in m[f]!!) "$i".padStart(2) else "  " }}")
    }
}

