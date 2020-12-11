package advent2020.day11

import advent2020.readResource
import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11PuzzleTest {

    val myPuzzleInput by lazy { readResource("day11") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("37", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("26", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("2289", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("2059", actual)
    }

}
