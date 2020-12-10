package advent2020.day10

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10PuzzleTest {

    val myPuzzleInput by lazy { readResource("day10") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            16
            10
            15
            5
            1
            11
            7
            19
            6
            12
            4
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("35", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            16
            10
            15
            5
            1
            11
            7
            19
            6
            12
            4
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("8", actual)
    }

    @Test
    fun examplePart2b() = runTest {
        val sampleInput =
            "28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3"
        val actual = part2(sampleInput)
        assertEquals("19208", actual)
    }

    @Test
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("1836", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("43406276662336", actual)
    }

    @Test
    fun vatiations1a() = runTest {
        assertEquals("1", part2("1"))
    }

    @Test
    fun vatiations2a() = runTest {
        assertEquals("2", part2("1\n2"))
    }

    @Test
    fun vatiations3a() = runTest {
        assertEquals("4", part2("1\n2\n3"))
    }

    @Test
    fun vatiations4a() = runTest {
        assertEquals("7", part2("1\n2\n3\n4"))
    }

}
