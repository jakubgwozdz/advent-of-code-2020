package advent2020.day23

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23PuzzleTest {

    val myPuzzleInput by lazy { readResource("day23") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """389125467"""
        val actual = part1(sampleInput)
        assertEquals("67384529", actual)
    }

    @Test
    @Ignore
    fun examplePart2() = runTest {
        val sampleInput = """389125467"""
        val actual = part2(sampleInput)
        assertEquals("149245887792", actual)
    }

    @Test
    fun test50cups1000times() = runTest("50 cups 1000 times") {
        val cups = game("""389125467""", 50, 1000)

        val actual = part2resultFormat(cups)
        assertEquals("2070", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("49576328", actual)
    }

    @Test
//    @Ignore
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("511780369955", actual)
    }

}
