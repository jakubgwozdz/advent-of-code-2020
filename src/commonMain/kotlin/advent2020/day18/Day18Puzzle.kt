package advent2020.day18

fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    val result = lines.map { groupTokens(parse(it).toList()).solve() }.sum()

    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    val result = lines.map { solveTokens2(parse(it).toList()) }.sum()

    return result.toString()
}

sealed class Node() {
    abstract fun solve(): Long
}

data class Value(val v: Long) : Node() {
    override fun solve() = v
    override fun toString() = v.toString()
}

data class Times(val n1: Node, val n2: Node) : Node() {
    override fun solve(): Long = n1.solve() * n2.solve()
    override fun toString() = "($n1 * $n2)"
}

data class Plus(val n1: Node, val n2: Node) : Node() {
    override fun solve(): Long = n1.solve() + n2.solve()
    override fun toString() = "($n1 + $n2)"
}

sealed class Token {
    open fun value(): Long = error("Not supported for ${this::class}")
    open fun node(): Node = error("Not supported for ${this::class}")
}

val Token.n get() = node()

data class Digits(val v: Long) : Token() {
    override fun toString(): String = v.toString()
    override fun node(): Node = Value(v)
    override fun value() = v
}

object TimesOp : Token() {
    override fun toString() = "*"
}

object PlusOp : Token() {
    override fun toString() = "+"
}

object OpenParenthesis : Token() {
    override fun toString() = "("
}

object CloseParenthesis : Token() {
    override fun toString() = ")"
}

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


fun groupTokens(tokens: List<Token>): Node = when {
    tokens.size == 1 -> tokens.first().n
    tokens.last() == CloseParenthesis -> {
        var j = tokens.size - 2
        var nested = 1
        while (nested > 0) {
            if (tokens[j] == CloseParenthesis) nested++
            if (tokens[j] == OpenParenthesis) nested--
            j--
        }
        if (j < 0) {
            groupTokens(tokens.drop(1).dropLast(1))
        } else {
            splitTokens(tokens, j, ::groupTokens)
        }
    }
    else -> splitTokens(tokens, tokens.size - 2, ::groupTokens)
}

fun splitTokens(tokens: List<Token>, j: Int, groupOp: (List<Token>) -> Node): Node {

    val op = when (tokens[j]) {
        PlusOp -> ::Plus
        TimesOp -> ::Times
        else -> error("invalid token in $tokens @ $j")
    }

    return op(groupTokens(tokens.take(j)), groupOp(tokens.drop(j + 1)))
}

fun solveTokens2(tokens: List<Token>): Long = when {
    tokens.size == 1 -> tokens[0].value()
    tokens.size == 3 && tokens[1] == PlusOp -> tokens[0].value() + tokens[2].value()
    tokens.size == 3 && tokens[1] == TimesOp -> tokens[0].value() * tokens[2].value()
    OpenParenthesis in tokens -> {
        val i = tokens.indexOf(OpenParenthesis)
        var j = i + 1
        var nested = 1
        while (nested > 0) {
            if (tokens[j] == OpenParenthesis) nested++
            if (tokens[j] == CloseParenthesis) nested--
            j++
        }
        solveTokens2(tokens.take(i) + Digits(solveTokens2(tokens.subList(i + 1, j - 1))) + tokens.drop(j))
    }
    PlusOp in tokens -> {
        val i = tokens.lastIndexOf(PlusOp)
        solveTokens2(
            tokens.take(i - 1) + Digits(solveTokens2(tokens.subList(i - 1, i + 2))) + tokens.drop(i + 2)
        )
    }
    tokens.size > 4 -> solveTokens2(
        listOf(Digits(solveTokens2(tokens.dropLast(2)))) + tokens.takeLast(2)
    )
    else -> error("invalid $tokens")
}
    .also { println("${tokens.joinToString("")} = $it") }

