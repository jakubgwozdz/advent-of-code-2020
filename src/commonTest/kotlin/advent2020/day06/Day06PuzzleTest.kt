package advent2020.day06

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06PuzzleTest {

    val myPuzzleInput by lazy { readResource("day06") }

    @Test
    fun exampleA() = runTest {
        val sampleInput = """
            abcx
            abcy
            abcz
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("6", actual)
    }

    @Test
    fun exampleB() = runTest {
        val sampleInput = """
            abc
            
            a
            b
            c
            
            ab
            ac
            
            a
            a
            a
            a
            
            b
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("11", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            abc
            
            a
            b
            c
            
            ab
            ac
            
            a
            a
            a
            a
            
            b
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("6", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("6310", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("3193", actual)
    }

}
