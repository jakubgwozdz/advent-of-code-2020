package advent2020.day05

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05PuzzleTest {

    val myPuzzleInput by lazy { readResource("day05") }

    @Test
    fun exampleA() = runTest {
        val sampleInput = """FBFBBFFRLR"""
        val actual = decode(sampleInput)
        assertEquals(357, actual)
    }

    @Test
    fun exampleB() = runTest {
        val sampleInput = """BFFFBBFRRR"""
        val actual = decode(sampleInput)
        assertEquals(567, actual)
    }

    @Test
    fun exampleC() = runTest {
        val sampleInput = """FFFBBBFRRR"""
        val actual = decode(sampleInput)
        assertEquals(119, actual)
    }

    @Test
    fun exampleD() = runTest {
        val sampleInput = """BBFFBBFRLL"""
        val actual = decode(sampleInput)
        assertEquals(820, actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("850", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("599", actual)
    }

}
