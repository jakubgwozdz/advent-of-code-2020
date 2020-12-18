package advent2020.day18

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18PuzzleTest {

    val myPuzzleInput by lazy { readResource("day18") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """1 + 2 * 3 + 4 * 5 + 6"""
        val actual = part1(sampleInput)
        assertEquals("71", actual)
    }

    @Test
    fun examplePart1a() = runTest {
        val sampleInput = """1 + (2 * 3) + (4 * (5 + 6))"""
        val actual = part1(sampleInput)
        assertEquals("51", actual)
    }

    @Test
    fun examplePart1b() = runTest {
        val sampleInput = """((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"""
        val actual = part1(sampleInput)
        assertEquals("13632", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """1 + 2 * 3 + 4 * 5 + 6"""
        val actual = part2(sampleInput)
        assertEquals("231", actual)
    }

    @Test
    fun examplePart2a() = runTest {
        val sampleInput = """1 + (2 * 3) + (4 * (5 + 6))"""
        val actual = part2(sampleInput)
        assertEquals("51", actual)
    }

    @Test
    fun examplePart2b() = runTest {
        val sampleInput = """5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"""
        val actual = part2(sampleInput)
        assertEquals("669060", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("14208061823964", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("320536571743074", actual)
    }

}
