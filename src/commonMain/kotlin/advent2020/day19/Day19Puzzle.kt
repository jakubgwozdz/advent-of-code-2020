package advent2020.day19

val inputRegex by lazy { """(\d+):(( "a")|( "b")|(((\s\d+)+)( \|((\s\d+)+))?))""".toRegex() }

sealed class Rule
data class CharRule(val c: Char) : Rule() {
    override fun toString() = "\"$c\""
}

data class ConcatRule(val l: List<Int>) : Rule() {
    override fun toString() = l.joinToString(" ")
}

data class AlternativeRule(val r1: ConcatRule, val r2: ConcatRule) : Rule() {
    override fun toString() = "$r1 | $r2"
}

fun part1(input: String): String {
    val lines = input.trim().lines()
    val rules = parseRules(lines)

    val result = lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }.count { message ->
        check(message.isNotEmpty())
        check(message.all { it == 'a' || it == 'b' })
        matches(rules[0]!!, rules, message, 0) == message.length
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
        v9.isNotBlank() -> v1.toInt() to AlternativeRule(ConcatRule(list1), ConcatRule(list2))
        v6.isNotBlank() -> v1.toInt() to ConcatRule(list1)
        else -> error("what to do with `$line` -> 1:`$v1`, 2:`$v2`, 3:`$v3`, 4:`$v4`, 5:`$v5`, 6:`$v6`, 7:`$v7`, 8:`$v8`, 9:`$v9` , 10:`$v10` ")
    }
}

fun matches(rule: Rule, rules: Map<Int, Rule>, message: String, start: Int, stack: List<String> = listOf("0")): Int {
//    println("==> matching ${message.substring(start)} with $rule")
    return when (rule) {
        is CharRule -> if (message[start] == rule.c) start + 1 else -1
        is AlternativeRule -> {
            val end = matches(rule.r1, rules, message, start, stack + "${stack.last()}_1")
            if (end >= 0) end else matches(rule.r2, rules, message, start, stack + "${stack.last()}_2")
        }
        is ConcatRule -> {
            var end = start
            rule.l.forEach {
                if (end < 0) return@forEach
                end = if (end == message.length) -1
                else matches(rules[it]!!, rules, message, end, stack + "$it")
            }
            end
        }
    }
        .also {
            if (it >= 0)
                println(
                    "<== matched ${message.take(start)}[${
                        message.substring(
                            start,
                            it
                        )
                    }]${message.substring(it)} with $rule ($stack)"
                )
            else
                println("<== failed  ${message.take(start)}[${message.substring(start)}  with $rule ($stack)")
        }
}

fun part2(input: String): String {
    val lines = input.trim().lines()
    val rules = parseRules(lines).toMutableMap()
        .apply {
            parseRule("8: 42 | 42 8").let { (id, r) -> this[id] = r }
            parseRule("11: 42 31 | 42 11 31").let { (id, r) -> this[id] = r }
        }
        .toMap()


    val result = lines.dropWhile { it.isNotBlank() }.filter { it.isNotBlank() }.count { message ->
        check(message.isNotEmpty())
        check(message.all { it == 'a' || it == 'b' })
        (matches(rules[0]!!, rules, message, 0) == message.length)
            .also { println("`$message` -> $it") }
    }

    return result.toString()


}

