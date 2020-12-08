package advent2020.day08

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day08PuzzleTest {

    val myPuzzleInput by lazy { readResource("day08") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            nop +0
            acc +1
            jmp +4
            acc +3
            jmp -3
            acc -99
            acc +1
            jmp -4
            acc +6      
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("5", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            nop +0
            acc +1
            jmp +4
            acc +3
            jmp -3
            acc -99
            acc +1
            jmp -4
            acc +6      
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("8", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("2080", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("2477", actual)
    }

}
