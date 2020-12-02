package advent2020.day02

internal data class ParsedLine(val number1: Int, val number2: Int, val char: Char, val password: String)

val regexp = Regex("(\\d+)-(\\d+) (\\w): (\\w*)")
internal fun parse(line: String): ParsedLine {
    val (g1, g2, g3, g4) = regexp.matchEntire(line)?.destructured
        ?: error("line `$line` does not match `$regexp`")
    return ParsedLine(g1.toInt(), g2.toInt(), g3.single(), g4)
}

fun part1(input: String): String {
    val passwords = input.trim().lines().map { parse(it) }
    val result = passwords
        .count { it.password.count { c -> c == it.char } in it.number1..it.number2 }
    return result.toString()
}

fun part2(input: String): String {
    val passwords = input.trim().lines().map { parse(it) }
    val result = passwords
        .count { (it.password[it.number1 - 1] == it.char) xor (it.password[it.number2 - 1] == it.char) }
    return result.toString()
}
