package advent2020.day15

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15PuzzleTest {

    val myPuzzleInput by lazy { readResource("day15") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """0,3,6"""
        val actual = part1(sampleInput)
        assertEquals("436", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """0,3,6"""
        val actual = part2(sampleInput)
        assertEquals("175594", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("240", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("505", actual)
    }

}
