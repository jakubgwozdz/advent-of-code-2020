package advent2020.day06

import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06InputConsistencyTest {
    @Test
    fun testFileConsistency() {
        val fromFile = (javaClass.getResource("jakubgwozdz").content as InputStream).use {
            InputStreamReader(it).readText()
        }
        val fromHardcoded = day06myPuzzleInput
        assertEquals(fromFile, fromHardcoded)
    }
}
