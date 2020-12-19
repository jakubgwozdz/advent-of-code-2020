package advent2020.day19

val inputRegex by lazy { """(\d+):(( "a")|( "b")|(((\s\d+)+)( \|((\s\d+)+))?))""".toRegex() }

sealed class Rule
data class CharRule(val c: Char) : Rule() {
    override fun toString() = "\"$c\""
}

data class ConcatRule(val l: List<Int>) : Rule() {
    override fun toString() = l.joinToString(" ")
}

data class AlternativeRule(val rules: List<ConcatRule>) : Rule() {
    override fun toString() = rules.joinToString(" | ")
}

fun part1(input: String): String {
    val lines = input.trim().lines()
    val rules = parseRules(lines)

    val result = lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }.count { message ->
        check(message.isNotEmpty())
        check(message.all { it == 'a' || it == 'b' })
        matches(rules[0]!!, rules, message, 0).any { it == message.length }
    }

    return result.toString()

}

fun parseRules(lines: List<String>) = lines.takeWhile { it.isNotBlank() }
    .map { line ->
        parseRule(line)
    }.toMap()

fun parseRule(line: String): Pair<Int, Rule> {
    val match = (inputRegex.matchEntire(line) ?: error("invalid line `$line`"))
    val (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) = match.destructured
    val list1 = v6.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val list2 = v9.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    return when {
        v3 == " \"a\"" -> v1.toInt() to CharRule('a')
        v4 == " \"b\"" -> v1.toInt() to CharRule('b')
        v9.isNotBlank() -> v1.toInt() to AlternativeRule(listOf(ConcatRule(list1), ConcatRule(list2)))
        v6.isNotBlank() -> v1.toInt() to ConcatRule(list1)
        else -> error("what to do with `$line` -> 1:`$v1`, 2:`$v2`, 3:`$v3`, 4:`$v4`, 5:`$v5`, 6:`$v6`, 7:`$v7`, 8:`$v8`, 9:`$v9` , 10:`$v10` ")
    }
}

fun matches(rule: Rule, rules: Map<Int, Rule>, message: String, start: Int): Sequence<Int> {
//    println("==> matching ${message.substring(start)} with $rule")
    return when (rule) {
        is CharRule -> if (message[start] == rule.c) sequenceOf(start + 1) else emptySequence()
        is AlternativeRule -> {
            rule.rules.asSequence()
                .flatMapIndexed { index, r -> matches(r, rules, message, start) }
                .filter { it >= 0 }
        }
        is ConcatRule -> {
            var result = matches(rules[rule.l[0]]!!, rules, message, start)


            if (rule.l.size > 1)
                result =
                    result.filter { it < message.length }.flatMap { matches(rules[rule.l[1]]!!, rules, message, it) }
            if (rule.l.size > 2)
                result =
                    result.filter { it < message.length }.flatMap { matches(rules[rule.l[2]]!!, rules, message, it) }
            if (rule.l.size > 3) TODO()
            result
        }
    }
}

fun part2(input: String): String {
    val lines = input.trim().lines()
    val rules = parseRules(lines).toMutableMap()
        .apply {
            parseRule("8: 42 | 42 8").let { (id, r) -> this[id] = r }
            parseRule("11: 42 31 | 42 11 31").let { (id, r) -> this[id] = r }
//            parseRule()
//            val possibleRules = (2..50).flatMap { s->
//                (1..s-1).map { e ->
//                    ConcatRule(IntArray(s){42}.toList() + IntArray(e){31}.toList())
//                }
//            }
//            this[0] = AlternativeRule(possibleRules)

        }
        .toMap()


    val result = lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }.count { message ->
        check(message.isNotEmpty())
        check(message.all { it == 'a' || it == 'b' })
        matches(rules[0]!!, rules, message, 0).any { it == message.length }
//            .also { println("`$message` -> $it") }
    }

    return result.toString()


}

