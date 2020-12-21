package advent2020.day18

fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    val result = lines.map { groupTokens(parse(it).toList()).solve() }.sum()

    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    val result = lines.map { groupTokens2(parse(it).toList()).solve() }.sum()

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

    return op(groupOp(tokens.take(j)), groupOp(tokens.drop(j + 1)))
}

fun groupTokens2(tokens: List<Token>): Node {
    val rpn = mutableListOf<Token>()
    val stack = mutableListOf<Token>()

    // shunting-yard
    tokens.forEach { token ->
        when (token) {
            PlusOp, TimesOp -> {
                while (stack.isNotEmpty()) {
                    val last = stack.last()
                    val prec2 = when (last) {
                        PlusOp -> 1
                        TimesOp -> 0
                        OpenParenthesis -> -1
                        else -> error("invalid stack $stack for token $token")
                    }
                    val prec1 = when (token) {
                        PlusOp -> 1
                        TimesOp -> 0
                        else -> error("not gonna happen")
                    }
                    if (prec2 >= prec1) {
                        rpn.add(stack.removeLast())
                    } else break
                }
                stack.add(token)
            }
            OpenParenthesis -> {
                stack.add(token)
            }
            CloseParenthesis -> {
                while (stack.last() != OpenParenthesis) rpn.add(stack.removeLast())
                stack.removeLast()
            }
            else -> {
                rpn.add(token)
            }
        }
    }
    while (stack.isNotEmpty()) rpn.add(stack.removeLast())

    val result = mutableListOf<Node>()
    rpn.forEach {
        when (it) {
            is Digits -> result.add(it.node())
            is PlusOp -> result.add(Plus(result.removeLast(), result.removeLast()))
            is TimesOp -> result.add(Times(result.removeLast(), result.removeLast()))
            else -> error("invalid token $it")
        }
    }

    return result.single()
}

