package advent2020.day00

import advent2020.emptyReporter
import advent2020.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Day00PuzzleTest {

    @Test
    fun jakubgwozdzPart1() = runTest {

        val actual = part1(myPuzzleInput, emptyReporter/*object:Day01ProgressReporter{
            override suspend fun reportPart1Progress(no: Int, total: Int, mass:Int, fuel:Int, sum: Int) {
                println("$no/$total: mass=$mass => fuel=$fuel, sum=$sum")
            }

        }*/)

        assertEquals("3297626", actual)

    }

}
