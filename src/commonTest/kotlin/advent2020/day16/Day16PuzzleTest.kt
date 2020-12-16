package advent2020.day16

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day16PuzzleTest {

    val myPuzzleInput by lazy { readResource("day16") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            class: 1-3 or 5-7
            row: 6-11 or 33-44
            seat: 13-40 or 45-50
            
            your ticket:
            7,1,14
            
            nearby tickets:
            7,3,47
            40,4,50
            55,2,20
            38,6,12
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("71", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            class: 0-1 or 4-19
            departure: 0-5 or 8-19
            seat: 0-13 or 16-19
            
            your ticket:
            11,12,13
            
            nearby tickets:
            3,9,18
            15,1,5
            5,14,9
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("11", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("21081", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("314360510573", actual)
    }

}
