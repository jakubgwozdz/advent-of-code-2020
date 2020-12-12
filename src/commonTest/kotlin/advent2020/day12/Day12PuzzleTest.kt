package advent2020.day12

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12PuzzleTest {

    val myPuzzleInput by lazy { readResource("day12") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            F10
            N3
            F7
            R90
            F11           
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("25", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            F10
            N3
            F7
            R90
            F11           
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("286", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("1007", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("41212", actual)
    }

}
