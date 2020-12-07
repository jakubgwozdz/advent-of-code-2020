package advent2020.day03

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03PuzzleTest {

    val myPuzzleInput by lazy { readResource("day03") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("7", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("336", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("232", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("3952291680", actual)
    }

}
