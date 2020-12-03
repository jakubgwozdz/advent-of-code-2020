package advent2020.day03

import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day03InputConsistencyTest {
    @Test
    fun testFileConsistency() {
        val fromFile = (javaClass.getResource("jakubgwozdz").content as InputStream).use {
            InputStreamReader(it).readText()
        }
        val fromHardcoded = day03myPuzzleInput
        assertEquals(fromFile, fromHardcoded)
    }
}
