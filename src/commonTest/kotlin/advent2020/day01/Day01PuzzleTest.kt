package advent2020.day01

import advent2020.emptyReceiver
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01PuzzleTest {
    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(day01myPuzzleInput, emptyReceiver)
        assertEquals("972576", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(day01myPuzzleInput, emptyReceiver)
        assertEquals("199300880", actual)
    }

}
