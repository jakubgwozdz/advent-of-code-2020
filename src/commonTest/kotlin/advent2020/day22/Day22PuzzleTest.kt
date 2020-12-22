package advent2020.day22

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22PuzzleTest {

    val myPuzzleInput by lazy { readResource("day22") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            Player 1:
            9
            2
            6
            3
            1
            
            Player 2:
            5
            8
            4
            7
            10
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("306", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            Player 1:
            9
            2
            6
            3
            1
            
            Player 2:
            5
            8
            4
            7
            10
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("291", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("35005", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest("Day22PuzzleTest.jakubgwozdzPart2") {
        val actual = part2(myPuzzleInput)
        assertEquals("32751", actual)
    }

}
