package advent2020.day01

import advent2020.InputData
import advent2020.createHeader

val inputData = InputData("1234567890")

@JsExport
fun createUI() {
    createHeader(1, inputData)
}
