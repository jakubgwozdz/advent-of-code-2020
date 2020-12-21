package advent2020.day21

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21PuzzleTest {

    val myPuzzleInput by lazy { readResource("day21") }

    @Test
    @Ignore
    fun examplePart1() = runTest {
        val sampleInput = """"""
        val actual = part1(sampleInput)
        assertEquals("", actual)
    }

    @Test
    @Ignore
    fun examplePart2() = runTest {
        val sampleInput = """"""
        val actual = part2(sampleInput)
        assertEquals("", actual)
    }

    @Test
    @Ignore
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("", actual)
    }

    @Test
    @Ignore
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("", actual)
    }

}
