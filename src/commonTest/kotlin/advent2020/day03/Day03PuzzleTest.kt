package advent2020.day03

import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03PuzzleTest {

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#"""
        val actual = part1(sampleInput)
        assertEquals("7", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#"""
        val actual = part2(sampleInput)
        assertEquals("336", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(day03myPuzzleInput)
        assertEquals("232", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(day03myPuzzleInput)
        assertEquals("3952291680", actual)
    }

}
