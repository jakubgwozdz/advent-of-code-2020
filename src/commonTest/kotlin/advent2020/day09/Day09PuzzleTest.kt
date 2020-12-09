package advent2020.day09

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day09PuzzleTest {

    val myPuzzleInput by lazy { readResource("day09") }

    @Test
//    fun examplePart1() = runTestExpect(NotImplementedError::class) {
    fun examplePart1() = runTest {
        val sampleInput = """
            35
            20
            15
            25
            47
            40
            62
            55
            65
            95
            102
            117
            150
            182
            127
            219
            299
            277
            309
            576
            """.trimIndent()
        val actual = firstInvalid(sampleInput.parsedData(), 5)
        assertEquals(127, actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            35
            20
            15
            25
            47
            40
            62
            55
            65
            95
            102
            117
            150
            182
            127
            219
            299
            277
            309
            576
            """.trimIndent()
        val actual = contiguousMinPlusMax(sampleInput.parsedData(), 127)
        assertEquals(62, actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("90433990", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("11691646", actual)
    }

}
