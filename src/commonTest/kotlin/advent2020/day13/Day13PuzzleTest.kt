package advent2020.day13

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day13PuzzleTest {

    val myPuzzleInput by lazy { readResource("day13") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """939
7,13,x,x,59,x,31,19"""
        val actual = part1(sampleInput)
        assertEquals("295", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """939
7,13,x,x,59,x,31,19"""
        val actual = part2(sampleInput)
        assertEquals("1068781", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("333", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("690123192779524", actual)
    }

}
