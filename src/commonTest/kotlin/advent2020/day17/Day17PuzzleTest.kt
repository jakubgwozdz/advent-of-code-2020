package advent2020.day17

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17PuzzleTest {

    val myPuzzleInput by lazy { readResource("day17") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            .#.
            ..#
            ###
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("112", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            .#.
            ..#
            ###
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("848", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
//    fun jakubgwozdzPart1() = runTestExpect(NotImplementedError::class) {
        val actual = part1(myPuzzleInput)
        assertEquals("306", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
//    fun jakubgwozdzPart2() = runTestExpect(NotImplementedError::class) {
        val actual = part2(myPuzzleInput)
        assertEquals("", actual)
    }

}
