package advent2020.day04

import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day04InputConsistencyTest {
    @Test
    fun testFileConsistency() {
        val fromFile = (javaClass.getResource("jakubgwozdz").content as InputStream).use {
            InputStreamReader(it).readText()
        }
        val fromHardcoded = day04myPuzzleInput
        assertEquals(fromFile, fromHardcoded)
    }
}
