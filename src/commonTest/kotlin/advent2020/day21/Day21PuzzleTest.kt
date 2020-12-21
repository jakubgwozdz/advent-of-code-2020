package advent2020.day21

import advent2020.readResource
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21PuzzleTest {

    val myPuzzleInput by lazy { readResource("day21") }

    @Test
//    @Ignore
    fun examplePart1() = runTest {
        val sampleInput = """
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("5", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
            trh fvjkl sbzzf mxmxvkd (contains dairy)
            sqjhc fvjkl (contains soy)
            sqjhc mxmxvkd sbzzf (contains fish)
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("mxmxvkd,sqjhc,fvjkl", actual)
    }

    @Test
//    @Ignore
    fun jakubgwozdzPart1() = runTest {
        val actual = part1(myPuzzleInput)
        assertEquals("2389", actual)
    }

    @Test
    fun jakubgwozdzPart2() = runTest {
        val actual = part2(myPuzzleInput)
        assertEquals("fsr,skrxt,lqbcg,mgbv,dvjrrkv,ndnlm,xcljh,zbhp", actual)
    }

}
