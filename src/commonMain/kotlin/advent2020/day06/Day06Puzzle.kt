package advent2020.day06

import advent2020.utils.groupSequence

fun part1(input: String): String {

    val result = input.groupSequence()
        .map {
            it.map(String::toSet).reduce { acc, person -> acc.union(person.toSet()) }.size
        }
        .sum()

    return result.toString()


}

fun part2(input: String): String {

    val result = input.groupSequence()
        .map {
            it.map(String::toSet).reduce { acc, person -> acc.intersect(person.toSet()) }.size
        }
        .sum()

    return result.toString()
}

