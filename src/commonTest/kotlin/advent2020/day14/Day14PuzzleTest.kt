package advent2020.day14

import advent2020.readResource
import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14PuzzleTest {

    val myPuzzleInput by lazy { readResource("day14") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
            mem[8] = 11
            mem[7] = 101
            mem[8] = 0
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("165", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            mask = 000000000000000000000000000000X1001X
            mem[42] = 100
            mask = 00000000000000000000000000000000X0XX
            mem[26] = 1
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("208", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("7817357407588", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("4335927555692", actual)
    }

}
