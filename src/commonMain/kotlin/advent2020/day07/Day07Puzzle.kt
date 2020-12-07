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

interface Day07ProgressLogger : ProgressLogger {
    suspend fun foundContaining(outerBag: String, innerBag: String, number: Int? = null, inOne: Int? = null) {}
}

val rootBag = "shiny gold"

suspend fun part1(input: String, logger: ProgressLogger = object : Day07ProgressLogger {}): String {

    val rules = parseRules(input)

    val possibilities = mutableSetOf<String>()

    val toCheck = mutableListOf(rootBag)
    while (toCheck.isNotEmpty()) {
        val current = toCheck.removeFirst()
        rules
            .filterKeys { it !in possibilities }
            .filterValues { current in it }
            .forEach { (outerBag, _) ->
                if (logger is Day07ProgressLogger) logger.foundContaining(outerBag, current)
                possibilities.add(outerBag)
                toCheck.add(outerBag)
            }
    }

    return possibilities.size.toString()
}

suspend fun part2(input: String, logger: ProgressLogger = object : Day07ProgressLogger {}): String {
    val rules = parseRules(input)

    return bagsInside(rootBag, rules, logger).toString()
}

suspend fun bagsInside(
    outerBag: String,
    rules: Map<String, Map<String, Int>>,
    logger: ProgressLogger,
    cache: MutableMap<String, Int> = mutableMapOf(), // cache is optional, reduces number of calls from ~90 to ~20
): Int {
    return cache[outerBag]
        ?: (rules[outerBag] ?: error("unknown bag $outerBag"))
            .map { (innerBag, count) ->
                count * (1 + (bagsInside(innerBag, rules, logger, cache).also {
                    if (logger is Day07ProgressLogger) logger.foundContaining(outerBag, innerBag, count, it)
                }))
            }
            .sum()
            .also { cache[outerBag] = it }
}

