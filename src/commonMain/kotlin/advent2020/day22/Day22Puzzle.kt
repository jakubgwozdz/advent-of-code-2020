package advent2020.day22

import advent2020.utils.groups

fun part1(input: String): String {
    val (p1, p2) = decks(input)
    combat(p1, p2)
    return score(p1, p2).toString()
}

fun part2(input: String): String {
    val (p1, p2) = decks(input)
    games = 0
    checks = 0
    recursiveCombat(p1, p2)
    println("; games $games checks $checks; ")
    return score(p1, p2).toString()
}

fun decks(input: String): Pair<MutableList<Int>, MutableList<Int>> = input.trim()
    .lineSequence()
    .groups { it.isBlank() }
    .map { l -> l.drop(1).map { it.toInt() }.toMutableList() }
    .toList()
    .let { (p1, p2) -> p1 to p2 }

fun score(p1: List<Int>, p2: List<Int>): Long {
    val p1sum = p1.reversed().mapIndexed { index, i -> (index + 1).toLong() * i }.sum()
    val p2sum = p2.reversed().mapIndexed { index, i -> (index + 1).toLong() * i }.sum()
    return p1sum + p2sum
}

fun combat(p1: MutableList<Int>, p2: MutableList<Int>) {
    while (p1.isNotEmpty() && p2.isNotEmpty()) {
        val card1 = p1.removeFirst()
        val card2 = p2.removeFirst()
        when {
            card1 > card2 -> p1 += listOf(card1, card2)
            card2 > card1 -> p2 += listOf(card2, card1)
            else -> error("same card $card1")
        }
    }
}

var games = 0L
var checks = 0L

fun recursiveCombat(p1: MutableList<Int>, p2: MutableList<Int>): Int {
    games++
    val memory = HashSet<Pair<List<Int>, List<Int>>>()
    while (p1.isNotEmpty() && p2.isNotEmpty()) {
        checks++
        val state = p1.toList() to p2.toList()
        if (state in memory) return 1
        memory += state
        val card1 = p1.removeFirst()
        val card2 = p2.removeFirst()
        val winner = when {
            card1 <= p1.size && card2 <= p2.size -> recursiveCombat(
                p1.take(card1).toMutableList(),
                p2.take(card2).toMutableList(),
            )
            card1 > card2 -> 1
            card1 < card2 -> 2
            else -> error("same card $card1")
        }
        when (winner) {
            1 -> p1 += listOf(card1, card2)
            2 -> p2 += listOf(card2, card1)
        }
    }
    return if (p1.isNotEmpty()) 1 else 2
}


