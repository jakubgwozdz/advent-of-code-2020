package advent2020.day06

import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06PuzzleTest {

    @Test
    fun exampleA() = runTest {
        val sampleInput = """abcx
abcy
abcz"""
        val actual = part1(sampleInput)
        assertEquals("6", actual)
    }

    @Test
    fun exampleB() = runTest {
        val sampleInput = """abc

a
b
c

ab
ac

a
a
a
a

b"""
        val actual = part1(sampleInput)
        assertEquals("11", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """abc

a
b
c

ab
ac

a
a
a
a

b"""
        val actual = part2(sampleInput)
        assertEquals("6", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
//    fun jakubgwozdzPart1() = runTestExpect(NotImplementedError::class) {
        val actual = part1(day06myPuzzleInput)
        assertEquals("6310", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
//    fun jakubgwozdzPart2() = runTestExpect(NotImplementedError::class) {
        val actual = part2(day06myPuzzleInput)
        assertEquals("3193", actual)
    }

}
