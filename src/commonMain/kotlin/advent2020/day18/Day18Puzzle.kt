package advent2020.day18

fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    val result = lines.map { solve(it) }.sum()

    return result.toString()
}

sealed class Node() {
    abstract fun solve(): Long
}

data class Value(val v: Long) : Node() {
    override fun solve() = v
}

data class Times(val n1: Node, val n2: Node) : Node() {
    override fun solve(): Long = n1.solve() * n2.solve()
}

data class Plus(val n1: Node, val n2: Node) : Node() {
    override fun solve(): Long = n1.solve() + n2.solve()
}

sealed class Token
data class Digits(val v: Long) : Token() {
    override fun toString(): String = v.toString()
}

object TimesOp : Token() {
    override fun toString() = "*"
}

object PlusOp : Token() {
    override fun toString() = "+"
}

fun solve(line: String, start: Int = 0, end: Int = line.length): Long {
//    println("parsing `$line` [$start until $end] `${line.substring(start, end)}`")
    val tokens = sequence {
        var i = start
        while (i < end) {
            if (line[i] in ('0'..'9')) {
                var j = i + 1
                while (j < line.length && line[j] in ('0'..'9')) j++
                yield(Digits(line.substring(i, j).toLong()))
                i = j
            } else if (line[i] == '*') {
                yield(TimesOp); i++
            } else if (line[i] == '+') {
                yield(PlusOp); i++
            } else if (line[i] == ' ') {
                i++
            } else if (line[i] == '(') {
                var j = i + 1
                var nested = 1
                while (nested > 0) {
                    if (line[j] == '(') nested++
                    if (line[j] == ')') nested--
                    j++
                }

                yield(Digits(solve(line, i + 1, j - 1)))
                i = j
            } else error("unknown `${line[i]}` @ $i")
        }
    }.toList()

    val result = solveTokens(tokens)

    return result
}

fun solveTokens(tokens: List<Token>): Long {
    var acc = (tokens[0] as Digits).v
    var i = 1
    while (i < tokens.size) {
        if (tokens[i] == PlusOp)
            acc += (tokens[i + 1] as Digits).v
        else if (tokens[i] == TimesOp)
            acc *= (tokens[i + 1] as Digits).v
        else TODO()
        i += 2
    }
    return acc
}


fun solve2(line: String, start: Int = 0, end: Int = line.length): Long {

//    println("parsing `$line` [$start until $end] `${line.substring(start, end)}`")
    val tokens = sequence {
        var i = start
        while (i < end) {
            if (line[i] in ('0'..'9')) {
                var j = i + 1
                while (j < line.length && line[j] in ('0'..'9')) j++
                yield(Digits(line.substring(i, j).toLong()))
                i = j
            } else if (line[i] == '*') {
                yield(TimesOp); i++
            } else if (line[i] == '+') {
                yield(PlusOp); i++
            } else if (line[i] == ' ') {
                i++
            } else if (line[i] == '(') {
                var j = i + 1
                var nested = 1
                while (nested > 0) {
                    if (line[j] == '(') nested++
                    if (line[j] == ')') nested--
                    j++
                }

                yield(Digits(solve2(line, i + 1, j - 1)))
                i = j
            } else error("unknown `${line[i]}` @ $i")
        }
    }.toList()

    val result = solveTokens2(tokens)

    return result

}

fun solveTokens2(tokens: List<Token>): Long {

    var newList = tokens
    while (newList.contains(PlusOp)) {
        val i = newList.indexOf(PlusOp)
        newList = newList.subList(0,
            i - 1) + Digits((newList[i - 1] as Digits).v + (newList[i + 1] as Digits).v) + newList.subList(i + 2,
            newList.size)
    }
    while (newList.contains(TimesOp)) {
        val i = newList.indexOf(TimesOp)
        newList = newList.subList(0,
            i - 1) + Digits((newList[i - 1] as Digits).v * (newList[i + 1] as Digits).v) + newList.subList(i + 2,
            newList.size)
    }

    return (newList.single() as Digits).v
}


fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    val result = lines.map { solve2(it) }.sum()

    return result.toString()
}

