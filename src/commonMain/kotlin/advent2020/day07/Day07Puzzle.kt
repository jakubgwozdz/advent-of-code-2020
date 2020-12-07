package advent2020.day07

import advent2020.ProgressLogger

val outerRegex = """(\w+ \w+) bags contain (.*)\.""".toRegex()
val innerRegex = """(\d+) (\w+ \w+) (bag|bags)""".toRegex()

fun parseRules(input: String) = input.trim().lines()
    .map { outerRegex.matchEntire(it)?.destructured ?: error("'$it' doesnt match") }
    .map { (outerBag, innerList) ->
        outerBag to innerList.split(", ")
            .filterNot { it == "no other bags" }
            .map { innerRegex.matchEntire(it)?.destructured ?: error("'$it' doesnt match") }
            .map { (count, innerBag) -> innerBag to count.toInt() }
            .toMap()
    }
    .toMap()

interface Part1ProgressLogger : ProgressLogger

fun part1(input: String): String {
    val rules = parseRules(input)

    val possibilities = mutableSetOf<String>()

    val toCheck = mutableListOf("shiny gold")
    while (toCheck.isNotEmpty()) {
        val current = toCheck.removeFirst()
        rules
            .filterKeys { it !in possibilities }
            .filterValues { current in it }
            .forEach { (outerBag, _) ->
                println("adding $outerBag as it can contain $current")
                possibilities.add(outerBag)
                toCheck.add(outerBag)
            }
    }

    return possibilities.size.toString()
}

fun part2(input: String): String {
    val rules = parseRules(input)

    return bagsInside("shiny gold", rules).toString()
}

fun bagsInside(
    outerBag: String,
    rules: Map<String, Map<String, Int>>,
    cache: MutableMap<String, Int> = mutableMapOf(), // cache is optional, reduces number of calls from ~90 to ~20
): Int {
    return cache[outerBag]
        ?: (rules[outerBag] ?: error("unknown bag $outerBag"))
            .map { (innerBag, count) -> count * (1 + bagsInside(innerBag, rules, cache)) }
            .sum()
            .also { cache[outerBag] = it }
}

