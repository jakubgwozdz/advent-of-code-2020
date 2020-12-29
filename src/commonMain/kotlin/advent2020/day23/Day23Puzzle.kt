package advent2020.day23

fun part1(input: String): String {
    val circle = Circle(input.trim())
    circle.makeNMoves(times = 100)
    return part1formatAnswer(circle)
}

fun part1formatAnswer(circle: Circle) = buildString {
    var n = circle.cups[1].next
    while (n.id != 1) {
        append(n.id)
        n = n.next
    }
}

fun part2(input: String): String {
    val circle = Circle(input.trim(), noOfCups = 1000000)
    circle.makeNMoves(times = 10000000)
    return part2formatAnswer(circle)
}

fun part2formatAnswer(circle: Circle): String {
    val one = circle.cups[1]
    val first = one.next.id
    val second = one.next.next.id
    val result = (first.toLong() * second.toLong()).toString()
    println(" $first * $second = $result ")
    return result
}

class Cup(val id: Int) {
    lateinit var next: Cup
}

class Circle(input: String, val noOfCups: Int = input.length) {
    val cups = Array(noOfCups + 1) { Cup(it) }.toList() // ignore idx 0, keep for readability
    var current: Cup

    init {

        repeat(input.length - 1) {
            val c = "${input[it]}".toInt()
            val n = "${input[it + 1]}".toInt()
            cups[c].next = cups[n]
        }

        val lastFromInput = "${input.last()}".toInt()
        val firstFromInput = "${input.first()}".toInt()
        cups[lastFromInput].next = cups[firstFromInput]

        if (noOfCups > input.length) {
            cups[lastFromInput].next = cups[input.length + 1]
            (input.length + 1 until noOfCups).forEach {
                cups[it].next = cups[it + 1]
            }

            cups[noOfCups].next = cups[firstFromInput]
        }

        current = cups[firstFromInput]
    }

    fun makeMove() {

        val c0 = current
        val c1 = c0.next
        val c2 = c1.next
        val c3 = c2.next
        val c4 = c3.next

        var destId = c0.id - 1
        if (destId == 0) destId = noOfCups
        while (destId == c1.id || destId == c2.id || destId == c3.id) {
            destId--
            if (destId == 0) destId = noOfCups
        }

        val d0 = cups[destId]
        val d1 = d0.next

        d0.next = c1
        c3.next = d1
        c0.next = c4

        current = c4
    }

    fun makeNMoves(times: Int) = repeat(times) { makeMove() }
}

// UNUSED
//
//fun Circle.format() = buildString(10000) {
//    var c = current
//    append("[")
//    repeat(noOfCups.coerceAtMost(52)) {
//        append(c.id)
//        if (it < noOfCups - 1) append(" ")
//        c = c.next
//    }
//    if (noOfCups > 52) append("...")
//    append("]")
//}
//
