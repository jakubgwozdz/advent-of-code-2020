package advent2020.day25

import advent2020.readResource
import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day25PuzzleTest {

    val myPuzzleInput by lazy { readResource("day25") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            5764801
            17807724
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("14897079", actual)
    }

    @Test
    fun examplePart2() = runTestExpect(NotImplementedError::class) {
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
//    fun jakubgwozdzPart2() = runTest {
    fun jakubgwozdzPart2() = runTestExpect(NotImplementedError::class) {
        val actual = part2(myPuzzleInput)
        assertEquals("", actual)
    }

}
