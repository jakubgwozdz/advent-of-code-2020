package advent2020.day16

import advent2020.readResource
import advent2020.runTest
import advent2020.runTestExpect
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
