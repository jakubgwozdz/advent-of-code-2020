package advent2020.day18

fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    // priority: parenthesis, rest
    val result = lines
        .map { shuntingYard(parse(it).toList()) { prev, _ -> prev != OpenParenthesis } }
        .map { rpn(it) }
        .sum()

    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    // priority: parenthesis, plus, times
    val result = lines
        .map { shuntingYard(parse(it).toList()) { prev, current -> current == TimesOp && prev == TimesOp || prev == PlusOp } }
        .map { rpn(it) }
        .sum()

    return result.toString()
}

sealed class Token
object TimesOp : Token()
object PlusOp : Token()
object OpenParenthesis : Token()
object CloseParenthesis : Token()
data class Digits(val v: Long) : Token()

private fun parse(line: String) = sequence {
    var i = 0
    while (i < line.length) {
        when (line[i]) {
            in '0'..'9' -> {
                var j = i + 1
                while (j < line.length && line[j] in ('0'..'9')) j++
                yield(Digits(line.substring(i, j).toLong()))
                i = j - 1
            }
            '*' -> yield(TimesOp)
            '+' -> yield(PlusOp)
            '(' -> yield(OpenParenthesis)
            ')' -> yield(CloseParenthesis)
            else -> error("unknown `${line[i]}` @ $i")
        }
        i++
        while (i < line.length && line[i] == ' ') i++ // skip spaces
    }
}

private fun shuntingYard(tokens: List<Token>, priorityOp: (Token, Token) -> Boolean): List<Token> {

    val rpn = mutableListOf<Token>()
    val opStack = mutableListOf<Token>()
    // shunting-yard
    tokens.forEach { token ->
        when (token) {
            PlusOp, TimesOp -> {
                while (opStack.isNotEmpty() && priorityOp(opStack.last(), token)) rpn.add(opStack.removeLast())
                opStack.add(token)
            }
            OpenParenthesis -> opStack.add(token)
            CloseParenthesis -> {
                while (opStack.last() != OpenParenthesis) rpn.add(opStack.removeLast())
                opStack.removeLast()
            }
            else -> rpn.add(token)
        }
    }
    while (opStack.isNotEmpty()) rpn.add(opStack.removeLast())
    return rpn.toList()
}

private fun rpn(rpn: List<Token>): Long {

    val result = mutableListOf<Long>()
    rpn.forEach {
        when (it) {
            is Digits -> result.add(it.v)
            is PlusOp -> result.add(result.removeLast() + result.removeLast())
            is TimesOp -> result.add(result.removeLast() * result.removeLast())
            else -> error("invalid token $it")
        }
    }

    return result.single()
}

