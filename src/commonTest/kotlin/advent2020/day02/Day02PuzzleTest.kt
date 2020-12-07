package advent2020.day02

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day02PuzzleTest {

    val myPuzzleInput by lazy { readResource("day02") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            1-3 a: abcde
            1-3 b: cdefg
            2-9 c: ccccccccc
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("2", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            1-3 a: abcde
            1-3 b: cdefg
            2-9 c: ccccccccc
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("1", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("434", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("509", actual)
    }

}
