package advent2020.day23

fun part1(input: String): String {
    val cups = game(input.trim(), times = 100)
    return part1resultFormat(cups)
}

fun part1resultFormat(circle: Circle) = buildString {
    var n = circle.cups[1].next
    while (n.id != 1) {
        append(n.id)
        n = n.next
    }
}

fun part2(input: String): String {
    val cups = game(input.trim(), noOfCups = 1000000, times = 10000000)
    return part2resultFormat(cups)
}

fun part2resultFormat(circle: Circle): String {
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

class Circle(input: String, val noOfCups: Int) {
    val cups = Array(noOfCups + 1) { Cup(it) }.toList() // ignore idx 0
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

        current = cups["${input[0]}".toInt()]
    }

    fun makeMove() {
        var destId = current.id - 1
        if (destId == 0) destId = noOfCups
        while (destId == current.next.id || destId == current.next.next.id || destId == current.next.next.next.id) {
            destId--
            if (destId == 0) destId = noOfCups
        }

        val dest = cups[destId]

        val currentPlus1 = current.next
        val currentPlus3 = current.next.next.next
        val currentPlus4 = current.next.next.next.next
        val destPlus1 = dest.next

        dest.next = currentPlus1
        current.next = currentPlus4
        currentPlus3.next = destPlus1

        current = current.next
    }

}

fun game(input: String, noOfCups: Int = input.length, times: Int): Circle {
    val circle = Circle(input, noOfCups)
    repeat(times) { circle.makeMove() }
    return circle
}

// UNUSED

fun Circle.format() = buildString(10000) {
    var c = current
    append("[")
    repeat(noOfCups.coerceAtMost(52)) {
        append(c.id)
        if (it < noOfCups - 1) append(" ")
        c = c.next
    }
    if (noOfCups > 52) append("...")
    append("]")
}

