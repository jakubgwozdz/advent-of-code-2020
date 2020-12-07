package advent2020.day05

import advent2020.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.js.canvas
import kotlinx.html.js.div
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

val day05puzzleContext by lazy { PuzzleContext(day05myPuzzleInput) }
val day05puzzleInfo = PuzzleInfo("day05", "Binary Boarding (Visual)", 5, 2020)

@JsExport
fun createUI() {

    createHeader(day05puzzleInfo, day05puzzleContext, readOnly = true)

    taskSection {
        title = "Part 1"
        subtitle = "Highest seatId"
        puzzleContext = day05puzzleContext
        task = suspending(::part1)
    }.buildInBody(document.body!!)

    Day05Part2SectionBuilder().buildInBody(document.body!!)

}

internal class Day05Part2Section(
    genericElements: GenericTaskSectionElements,
    val canvas: HTMLCanvasElement
) : GenericTaskSection(genericElements), Day05Part2ProgressReceiver {

    private var shouldUpdate = false
    var timer: Int? = window.setInterval(::flush, 16)

    override suspend fun foundSeat(no: Int, total: Int, seatId: Int, seatCode: String) {
        seats[seatId] = seatCode
        shouldUpdate = true
        progressField.value(no, total)
        delayIfChecked(16)
    }

    val seats = mutableMapOf<Int, String>()

    private fun flush() {
        (canvas.getContext("2d") as CanvasRenderingContext2D).let { ctx ->
            val frontRow = seats.keys.minOrNull()?.let { it/8}
            val backRow = seats.keys.maxOrNull()?.let { it/8}

            val xScale = 25.0
            val yScale = 18.0

            if (frontRow != null && backRow != null) {
                ctx.fillStyle = "#111"
                canvas.height =
                    (30 + yScale * (backRow - frontRow + 1)).toInt()
                ctx.fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

                ctx.fillStyle = "#333"
                (frontRow..backRow).forEach { row ->
                    (0..7).forEach { column ->
                        val aisle = if (column < 2) -15 else if (column > 5) 15 else 0
                        val x =
                            canvas.width / 2.0 + (column - 4) * xScale + aisle
                        val y = (row - frontRow) * yScale + 10.0

                        ctx.fillStyle = "#333"
                        ctx.fillRect(x, y, 21.0, 16.0)

                        val siteId = row * 8 + column
                        if (siteId in seats) {
                            ctx.fillStyle = "#963"
                            ctx.fillRect(x+1, y+1, 19.0, 14.0)
                            ctx.fillStyle = "#321"
                        } else
                        if (siteId !in seats && siteId+1 in seats && siteId-1 in seats) {
                            ctx.fillStyle = "#fff"
                        } else {
                            ctx.fillStyle = "#000"
                        }
                        ctx.fillText("$siteId", x+2, y+12)

                    }
                }

            } else {
                canvas.height = 0
            }
        }
        shouldUpdate = false
    }


    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        seats.clear()
        shouldUpdate = true
    }
}


internal class Day05Part2SectionBuilder : TaskSectionBuilder() {
    init {
        title = "Part 2"
        subtitle = "Missing seatId"
        puzzleContext = day05puzzleContext
        task = ::part2
        delay = true
    }

    lateinit var canvasElem: HTMLCanvasElement

    override fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {
        with(div) {
            div("has-text-centered") {
                canvasElem = canvas {
                    width = "270"
                    height = "10"
                }
            }
        }
    }

    override fun constructObject(): TaskSection {
        return Day05Part2Section(
            genericElements(),
            canvasElem
        )
    }

}

