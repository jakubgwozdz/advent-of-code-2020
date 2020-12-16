package advent2020.day16

data class Field(val name: String, val range1: LongRange, val range2: LongRange) {
    operator fun contains(v: Long) = v in range1 || v in range2
}

val fieldRegex = """(.+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
private fun fields(lines: Sequence<String>): List<Field> = lines
    .takeWhile { it.isNotBlank() }
    .map { fieldRegex.matchEntire(it)?.destructured ?: error("invalid line `$it`") }
    .map { (name, start1, end1, start2, end2) ->
        Field(name,
            start1.toLong()..end1.toLong(),
            start2.toLong()..end2.toLong())
    }
    .toList()


fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    val fields = fields(lines)

    val result = lines.drop(fields.size + 5).map { nearby ->
        nearby.split(',').map { it.toLong() }
            .filterNot { v -> fields.any { v in it } }
            .sum()
    }.sum()

    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    val fields = fields(lines)

    val myTicket = lines.drop(fields.size + 2).first().split(',').map { it.toLong() }
//        .also { println("my Ticket: $it") }

    val fieldIndices = fields.map { it.name }.associateWith { fields.indices.toMutableSet() }

    lines.drop(fields.size + 5)
        .map { it.split(',').map(String::toLong) }
        .filter { nearby -> nearby.all { v -> fields.any { v in it } } }
        .forEach { ticket ->
            ticket.forEachIndexed { index, v ->
                fields.filterNot { v in it }
                    .map { it.name }
                    .forEach {
                        println("`$it` can't be at $index because of $ticket")
                        fieldIndices[it]!! -= index
                    }
            }
        }

//    display(fieldIndices)
    while (doSearchStep(fieldIndices)) {
//        display(fieldIndices)
    }

    return fields.asSequence()
        .map { it.name }
        .filter { it.startsWith("departure") }
        .map { fieldIndices[it]!!.single() }
        .map { myTicket[it] }
        .fold(1L) { a, b -> a * b }.toString()
}

fun doSearchStep(fieldIndices: Map<String, MutableSet<Int>>): Boolean {
    var changed = false

    val lastIndex = fieldIndices.size - 1
    val fieldsWithOneIndex = fieldIndices
        .filterValues { it.size == 1 }
        .map { (f, v) -> f to v.single() }

    val indicesWithOneField = (0..lastIndex)
        .mapNotNull { index ->
            fieldIndices.entries
                .singleOrNull { (_, v) -> index in v }
                ?.let { (f, _) -> f to index }
        }

    val newFieldsWithOneIndex = fieldsWithOneIndex - indicesWithOneField
    val newIndicesWithOneField = indicesWithOneField - fieldsWithOneIndex

    newFieldsWithOneIndex.forEach { (f, i) ->
        println("`$f` can be only at $i")
        fieldIndices.forEach { (f1, s) -> if (f1 != f && i in s) s -= i.also { changed = true } }
    }
    newIndicesWithOneField.forEach { (f, i) ->
        println("at $i can be only `$f`")
        fieldIndices.forEach { (f1, s) -> if (f1 == f && s.size > 1) s.retainAll(setOf(i)).also { changed = true } }
    }

    return changed
}

fun display(m: Map<String, Set<Int>>) {
    println()
    val lastIndex = m.size - 1
    println("${": ".padStart(32)}${(0..lastIndex).map { i -> "$i".padStart(2) }}")
    m.forEach { (f, s) ->
        println("${f.padStart(30)}: ${
            (0..lastIndex).map { i -> if (i in s) "><" else "  " }
        }")
    }
}

