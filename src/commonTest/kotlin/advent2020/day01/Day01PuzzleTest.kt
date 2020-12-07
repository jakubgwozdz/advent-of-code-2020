package advent2020.day01

import advent2020.readResource
import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01PuzzleTest {

    val myPuzzleInput by lazy { readResource("day01") }

    @Test
    fun puzzleTextPart1() = runTest {
        val exampleInput = """
            1721
            979
            366
            299
            675
            1456
            """.trimIndent()
        val actual = part1(exampleInput)
        assertEquals("514579", actual)
    }

    @Test
    fun puzzleTextPart2() = runTest {
        val exampleInput = """
            1721
            979
            366
            299
            675
            1456
            """.trimIndent()
        val actual = part2(exampleInput)
        assertEquals("241861950", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("972576", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("199300880", actual)
    }

    @Test
    fun duplicatedValue() = runTest {
        val numbers = listOf(1, 1010, 1010, 2018)

        val i1 = findIndex(numbers, 2020)
        val v1 = numbers[i1]

        assertEquals(1010, v1)

    }

    @Test
    fun halfValue() = runTestExpect(IndexOutOfBoundsException::class) {
        val numbers = listOf(1, 1010, 1011, 2018)

        val i1 = findIndex(numbers, 2020)
        val v1 = numbers[i1]

        assertEquals(1010, v1)

    }

    @Test
    fun duplicatedValueInPart2() = runTest {
        val numbers = listOf(1, 100, 100, 1820, 2018)

        val i1 = findIndex(numbers, 1920, 1)
        val v1 = numbers[i1]

        assertEquals(100, v1)

    }

    @Test
    fun halfValueInPart2() = runTest {
        val numbers = listOf(1, 100, 101, 102, 1817, 1820)

        val i1 = findIndex(numbers, 1920, 1)

        assertEquals(-1, i1)

    }

}
