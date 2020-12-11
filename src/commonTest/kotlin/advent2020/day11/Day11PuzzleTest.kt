package advent2020.day11

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11PuzzleTest {

    val myPuzzleInput by lazy { readResource("day11") }

    @Test
    fun examplePart1() {
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
    fun examplePart2() {
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
    fun jakubgwozdzPart1() {
        val actual = part1(myPuzzleInput)
        assertEquals("2289", actual)
    }

    @Test
    fun jakubgwozdzPart2() {
        val actual = part2(myPuzzleInput)
        assertEquals("2059", actual)
    }

}
