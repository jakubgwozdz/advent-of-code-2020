package advent2020.day22

import advent2020.readResource
import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22PuzzleTest {

    val myPuzzleInput by lazy { readResource("day22") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """"""
        val actual = part1(sampleInput)
        assertEquals("", actual)
    }

    @Test
    fun examplePart2() = runTestExpect(NotImplementedError::class) {
        val sampleInput = """"""
        val actual = part2(sampleInput)
        assertEquals("", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
//    fun jakubgwozdzPart1() = runTestExpect(NotImplementedError::class) {
        val actual = part1(myPuzzleInput)
        assertEquals("", actual)
    }

    @Test
//    fun jakubgwozdzPart2() = runTest {
    fun jakubgwozdzPart2() = runTestExpect(NotImplementedError::class) {
        val actual = part2(myPuzzleInput)
        assertEquals("", actual)
    }

}
