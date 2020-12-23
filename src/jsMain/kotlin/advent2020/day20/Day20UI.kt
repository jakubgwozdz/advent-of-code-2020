package advent2020.day20

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.suspending
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.js.canvas
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

val day20puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day20puzzleInfo = PuzzleInfo("day20", "Jurassic Jigsaw", 20, 2020)

@JsExport
fun createUI() {

    createHeader(day20puzzleInfo, day20puzzleContext)

    day20part1Section {
        title = "Part 1"
        puzzleContext = day20puzzleContext
        task = ::part1
        delay = true
    }.buildInBody(document.body!!)

    taskSection {
        title = "Part 2"
        puzzleContext = day20puzzleContext
        task = suspending(::part2)
    }.buildInBody(document.body!!)

}

class Day20Part1Section(genericElements: GenericTaskSectionElements, val canvas: HTMLCanvasElement) :
    GenericTaskSection(genericElements), Day20ProgressLogger {

    private var shouldUpdate = false
    var timer: Int? = window.setInterval(::flush, 16)

    private var tiles: List<Tile> = emptyList()

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        shouldUpdate = true
    }

    override suspend fun foundTiles(tiles: Map<Long, Tile>) {
        this.tiles = tiles.values.toList()
    }

    private fun flush() = canvas.getContext("2d").let { ctx ->
        ctx as CanvasRenderingContext2D
        val w = ctx.canvas.parentElement?.clientWidth!!
        ctx.canvas.width = w
        ctx.canvas.height = w
        ctx.scale(w/1800.0, w/1800.0)

        ctx.fillStyle = "#abc"
        ctx.fillRect(0.0, 0.0, 1800.0, 1800.0)
        ctx.fillStyle = "#14c"
        ctx.fillRect(4.0, 4.0, 1792.0, 1792.0)

        tiles.forEachIndexed { index, tile ->
            val r = index / 12
            val c = index % 12
            val oldTransform = ctx.getTransform()

            ctx.translate(r * 150.0+75.0, c * 150.0 +75.0 )
            ctx.scale(0.8, 0.8)
            ctx.rotate(tile.id.toDouble())
            ctx.translate(-75.0, -67.0)


            ctx.fillStyle = "#abc"
            ctx.fillRect(-2.0,-2.0,154.0, 154.0)
            ctx.fillStyle = "#25d"
            ctx.fillRect(0.0, 0.0, 150.0, 150.0)

            ctx.strokeStyle = "#abc"
            ctx.beginPath()
            ctx.moveTo(0.0,0.0)
            ctx.lineTo(5.0,-20.0)
            ctx.lineTo(70.0,-20.0)
            ctx.lineTo(75.0,0.0)
            ctx.stroke()

            ctx.fillStyle = "#fff"
            ctx.font = "18px sans-serif"
            ctx.fillText("${tile.id}", 10.0, -3.0, 30.0)

            ctx.fillStyle="#def"
            (1..tile.size-2).forEach { y ->
                (1..tile.size - 2).forEach { x ->
                    if (tile.at(y,x)=='#')
                        ctx.fillRect(x*15.0, y*15.0, 15.0, 15.0)
                }
            }
            ctx.fillStyle="#89e"
            (1..tile.size-2).forEach { y ->
                if (tile.at(y,0)=='#')
                    ctx.fillRect(0*15.0, y*15.0, 15.0, 15.0)
                if (tile.at(y,tile.size-1)=='#')
                    ctx.fillRect((tile.size-1)*15.0, y*15.0, 15.0, 15.0)
            }
            (0..tile.size-1).forEach { x ->
                if (tile.at(0,x)=='#')
                    ctx.fillRect(x*15.0, 0*15.0, 15.0, 15.0)
                if (tile.at(tile.size-1, x)=='#')
                    ctx.fillRect(x*15.0, (tile.size-1)*15.0, 15.0, 15.0)
            }
            ctx.setTransform(oldTransform)
        }

    }.also {
        shouldUpdate = false
    }
}

class Day20Part1SectionBuilder : TaskSectionBuilder() {
    lateinit var canvas: HTMLCanvasElement
    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) {
        with(bodyBuilder) {
            canvas = canvas {
//                width = "2400"
//                height = "2400"
            }
        }
    }

    override fun constructObject() = Day20Part1Section(genericElements(), canvas)
}

fun day20part1Section(op: Day20Part1SectionBuilder.() -> Unit) = Day20Part1SectionBuilder().apply(op)
