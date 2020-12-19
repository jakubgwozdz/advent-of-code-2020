package advent2020.day19

val inputRegex by lazy { """(\d+):(( "a")|( "b")|(((\s\d+)+)( \|((\s\d+)+))?))""".toRegex() }

sealed class Rule
data class CharRule(val c: Char) : Rule()

data class ConcatRule(val l: List<Int>) : Rule()

data class AlternativeRule(val rules: List<ConcatRule>) : Rule()

fun part1(input: String): String {
    val lines = input.trim().lines()
    val rules = parseRules(lines.takeWhile { it.isNotBlank() })

    return validate(lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }, rules).toString()

}

fun part2(input: String): String {
    val lines = input.trim().lines()
    val rules = parseRules(lines.takeWhile { it.isNotBlank() })
        .toMutableMap()
        .apply {
            parseRule("8: 42 | 42 8").let { (id, r) -> this[id] = r }
            parseRule("11: 42 31 | 42 11 31").let { (id, r) -> this[id] = r }
        }
        .toMap()

    return validate(lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }, rules).toString()
}

fun parseRules(lines: List<String>) = lines
    .map { line ->
        parseRule(line)
    }.toMap()

fun parseRule(rules: String): Pair<Int, Rule> {
    val match = (inputRegex.matchEntire(rules) ?: error("invalid line `$rules`"))
    val (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) = match.destructured
    val list1 = v6.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val list2 = v9.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    return when {
        v3 == " \"a\"" -> v1.toInt() to CharRule('a')
        v4 == " \"b\"" -> v1.toInt() to CharRule('b')
        v9.isNotBlank() -> v1.toInt() to AlternativeRule(listOf(ConcatRule(list1), ConcatRule(list2)))
        v6.isNotBlank() -> v1.toInt() to ConcatRule(list1)
        else -> error("what to do with `$rules` -> 1:`$v1`, 2:`$v2`, 3:`$v3`, 4:`$v4`, 5:`$v5`, 6:`$v6`, 7:`$v7`, 8:`$v8`, 9:`$v9` , 10:`$v10` ")
    }
}

fun validate(
    messages: List<String>,
    rules: Map<Int, Rule>
) = messages.count { message ->
    check(message.isNotEmpty())
    check(message.all { it == 'a' || it == 'b' })
    matches(rules[0]!!, rules, message, 0).any { it == message.length }
}

fun matches(rule: Rule, rules: Map<Int, Rule>, message: String, start: Int): Sequence<Int> {
    return when (rule) {
        is CharRule -> if (message[start] == rule.c) sequenceOf(start + 1) else emptySequence()
        is AlternativeRule -> {
            rule.rules.asSequence()
                .flatMap { matches(it, rules, message, start) }
                .filter { it >= 0 }
        }
        is ConcatRule -> {
            rule.l.asSequence().drop(1).fold(matches(rules[rule.l[0]]!!, rules, message, start)) { acc, i ->
                acc.filter { it < message.length }.flatMap { matches(rules[i]!!, rules, message, it) }
            }
        }
    }
}

