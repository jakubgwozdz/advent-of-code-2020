package advent2020.day01

import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day01InputConsistencyTest {
    @Test
    fun testFileConsistency() {
        val fromFile = (javaClass.getResource("jakubgwozdz").content as InputStream).use {
            InputStreamReader(it).readText()
        }
        val fromHardcoded = day01myPuzzleInput
        assertEquals(fromFile, fromHardcoded)
    }
}
