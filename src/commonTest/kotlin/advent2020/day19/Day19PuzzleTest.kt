package advent2020.day19

import advent2020.readResource
import advent2020.runTest
import advent2020.runTestExpect
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day19PuzzleTest {

    val myPuzzleInput by lazy { readResource("day19") }

    @Test
    fun examplePart1() = runTest {
        val sampleInput = """
            0: 4 1 5
            1: 2 3 | 3 2
            2: 4 4 | 5 5
            3: 4 5 | 5 4
            4: "a"
            5: "b"
            
            ababbb
            bababa
            abbbab
            aaabbb
            aaaabbb
            """.trimIndent()
        val actual = part1(sampleInput)
        assertEquals("2", actual)
    }

    @Test
    fun examplePart2() = runTest {
        val sampleInput = """
            42: 9 14 | 10 1
            9: 14 27 | 1 26
            10: 23 14 | 28 1
            1: "a"
            11: 42 31
            5: 1 14 | 15 1
            19: 14 1 | 14 14
            12: 24 14 | 19 1
            16: 15 1 | 14 14
            31: 14 17 | 1 13
            6: 14 14 | 1 14
            2: 1 24 | 14 4
            0: 8 11
            13: 14 3 | 1 12
            15: 1 | 14
            17: 14 2 | 1 7
            23: 25 1 | 22 14
            28: 16 1
            4: 1 1
            20: 14 14 | 1 15
            3: 5 14 | 16 1
            27: 1 6 | 14 18
            14: "b"
            21: 14 1 | 1 14
            25: 1 1 | 1 14
            22: 14 14
            8: 42
            26: 14 22 | 1 20
            18: 15 15
            7: 14 5 | 1 21
            24: 14 1
            
            abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
            bbabbbbaabaabba
            babbbbaabbbbbabbbbbbaabaaabaaa
            aaabbbbbbaaaabaababaabababbabaaabbababababaaa
            bbbbbbbaaaabbbbaaabbabaaa
            bbbababbbbaaaaaaaabbababaaababaabab
            ababaaaaaabaaab
            ababaaaaabbbaba
            baabbaaaabbaaaababbaababb
            abbbbabbbbaaaababbbbbbaaaababb
            aaaaabbaabaaaaababaa
            aaaabbaaaabbaaa
            aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
            babaaabbbaaabaababbaabababaaab
            aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("12", actual)
    }

    @Test
    fun examplePart2a() = runTest {
        val sampleInput = """
            1: "a"
            14: "b"
            4: 1 1
            22: 14 14
            24: 14 1
            15: 1 | 14
            25: 1 1 | 1 14
            19: 14 1 | 14 14
            21: 14 1 | 1 14
            6: 14 14 | 1 14
            20: 14 14 | 1 15
            18: 15 15
            12: 24 14 | 19 1
            16: 15 1 | 14 14
            28: 16 1
            23: 25 1 | 22 14
            10: 23 14 | 28 1
            26: 14 22 | 1 20
            27: 1 6 | 14 18
            9: 14 27 | 1 26
            5: 1 14 | 15 1
            3: 5 14 | 16 1
            13: 14 3 | 1 12
            2: 1 24 | 14 4
            7: 14 5 | 1 21
            17: 14 2 | 1 7
            42: 9 14 | 10 1
            31: 14 17 | 1 13
            8: 42
            11: 42 31
            0: 8 11
            
            babbbbaabbbbbabbbbbbaabaaabaaa
            """.trimIndent()
        val actual = part2(sampleInput)
        assertEquals("1", actual)
    }

    @Test
//    @Ignore
    fun jakubgwozdzPart1() = runTest {
//    fun jakubgwozdzPart1() = runTestExpect(NotImplementedError::class) {
        val actual = part1(myPuzzleInput)
        assertEquals("224", actual)
    }

    @Test
    @Ignore
//    fun jakubgwozdzPart2() = runTest {
    fun jakubgwozdzPart2() = runTestExpect(NotImplementedError::class) {
        val actual = part2(myPuzzleInput)
        assertEquals("", actual)
    }

}
