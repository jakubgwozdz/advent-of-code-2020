package advent2020.day19

val inputRegex by lazy { """(\d+):(( "a")|( "b")|(((\s\d+)+)( \|((\s\d+)+))?))""".toRegex() }

sealed class Rule

//data class CharRule(val c: Char) : Rule()
data class StringRule(val s: String) : Rule()
data class ConcatRule(val l: List<Int>) : Rule()

data class AlternativeRule(val rules: List<Rule>) : Rule()

fun part1(input: String): String {
    val lines = input.trim().lines()
    val rules = parseRules(lines.takeWhile { it.isNotBlank() })

    tests = 0L
    return validate(lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }, rules).toString()
        .also { println("tests: $tests") }

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
    tests = 0L

    return validate(lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }, rules).toString()
        .also { println("tests: $tests") }
}

fun parseRules(lines: List<String>): Map<Int, Rule> {
    val map = lines
        .map { line ->
            parseRule(line)
        }.toMap().toMutableMap()

    // optimization
    var check = true
    while (check) {
        check = false
        map.keys.toSet().filter { it != 0 }.forEach { id ->
            val r = map[id]
            if (r is ConcatRule) {
                val newR = optConcat(r, map)
                if (r != newR) map[id] = newR.also { check = true }
            }
            if (r is AlternativeRule) {
                val newR = optAlternative(r, map)
                if (r != newR) map[id] = newR.also { check = true }
            }
        }

    }
    return map.toMap()
}

fun optConcat(r: ConcatRule, map: Map<Int, Rule>) = when {
    r.l.all { map[it] is StringRule } -> {
        val newRule = StringRule(r.l.joinToString("") { (map[it] as StringRule).s })
        newRule
    }
    r.l.all { map[it] is ConcatRule } -> {
        val newRule = ConcatRule(r.l.fold(emptyList()) { acc, i -> acc + (map[i] as ConcatRule).l })
        newRule
    }
    else -> r
}

fun optAlternative(r: AlternativeRule, map: Map<Int, Rule>): Rule {
    return AlternativeRule(r.rules.map { if (it is ConcatRule) optConcat(it, map) else it })
//        .let { r2->
//            if (r2.rules.all { it is ConcatRule && map[it.l.first()] is StringRule }) {
//                val f = map[(r2.rules.first() as ConcatRule).l.first()] as StringRule
//                if (r2.rules.all { (it as ConcatRule).l.first().let { map [it] as StringRule}.s == f.s} ) {
//                    ConcatRule(listOf(f))
//                } else r2
//            } else r2
//        }
}


fun parseRule(rules: String): Pair<Int, Rule> {
    val match = (inputRegex.matchEntire(rules) ?: error("invalid line `$rules`"))
    val (v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) = match.destructured
    val list1 = v6.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val list2 = v9.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    return when {
        v3 == " \"a\"" -> v1.toInt() to StringRule("a")
        v4 == " \"b\"" -> v1.toInt() to StringRule("b")
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

var tests = 0L

fun matches(rule: Rule, rules: Map<Int, Rule>, message: String, start: Int): Sequence<Int> {
    tests++
    return when (rule) {
//        is CharRule -> if (message[start] == rule.c) sequenceOf(start + 1) else emptySequence()
        is StringRule -> if (message.startsWith(rule.s, start)) sequenceOf(start + rule.s.length) else emptySequence()
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

