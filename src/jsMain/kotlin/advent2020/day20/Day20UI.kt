package advent2020.day20

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import advent2020.taskSection
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.js.canvas
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.math.PI

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
        task = ::part2
    }.buildInBody(document.body!!)

}

data class TileInfo(val tile: Tile, val row: Int, val column: Int, val angle: Double = (tile.id % 360 - 180)* PI/180, val matches: Map<Edge, Long> = emptyMap())

class Day20Part1Section(genericElements: GenericTaskSectionElements, val canvas: HTMLCanvasElement) :
    GenericTaskSection(genericElements), Day20ProgressLogger {

    private var shouldUpdate = false
    var timer: Int? = window.setInterval(::flush, 16)

    private val tiles: MutableMap<Long, TileInfo> = mutableMapOf()

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        tiles.clear()
        shouldUpdate = true
    }

    override suspend fun foundTiles(tiles: Map<Long, Tile>) {
        var index = 0
        tiles.forEach { (id, tile) ->
            val r = index / 12
            val c = index % 12
            index++
            this.tiles[tile.id] = TileInfo(tile, r, c)
        }
        shouldUpdate = true
        delayIfChecked(16)
    }

    override suspend fun foundMatch(id1: Long, edge: Edge, id2: Long) {
        tiles[id1] = tiles[id1]!!.run { copy(matches = matches + (edge to id2)) }
        shouldUpdate = true
        delayIfChecked(16)
    }

    private fun flush() = canvas.getContext("2d").let { ctx ->
        ctx as CanvasRenderingContext2D
        val w = ctx.canvas.parentElement?.clientWidth!!
        ctx.canvas.width = w
        ctx.canvas.height = w
        ctx.scale(w / 1800.0, w / 1800.0)

        ctx.fillStyle = "#abc"
        ctx.fillRect(0.0, 0.0, 1800.0, 1800.0)
        ctx.fillStyle = "#14c"
        ctx.fillRect(4.0, 4.0, 1792.0, 1792.0)

        // tiles
        tiles.forEach { (id, tileInfo) ->
            val oldTransform = ctx.getTransform()
            val tile = tileInfo.tile

            setTileTransform(ctx, tileInfo)
            val ts = 20.0 // tile size

            ctx.fillStyle = "#abc"
            ctx.fillRect(-2.0, -2.0, 204.0, 204.0)
            ctx.fillStyle = "#25d"
            ctx.fillRect(0.0, 0.0, 200.0, 200.0)

            ctx.strokeStyle = "#abc"
            ctx.beginPath()
            ctx.moveTo(0.0, 0.0)
            ctx.lineTo(5.0, -30.0)
            ctx.lineTo(90.0, -30.0)
            ctx.lineTo(100.0, 0.0)
            ctx.stroke()

            ctx.fillStyle = "#fff"
            ctx.font = "30px Lato"
            ctx.scale(2.0,1.0)
            ctx.fillText("${id}", 10.0, -5.0, 30.0)
            ctx.scale(0.5,1.0)

            ctx.fillStyle = "#def"
            (1..tile.size - 2).forEach { y ->
                (1..tile.size - 2).forEach { x ->
                    if (tile.at(y, x) == '#')
                        ctx.fillRect(x * ts, y * ts, ts, ts)
                }
            }
            // edges
            ctx.fillStyle = "#89e"
            (1..tile.size - 2).forEach { y ->
                if (tile.at(y, 0) == '#')
                    ctx.fillRect(0 * ts, y * ts, ts, ts)
                if (tile.at(y, tile.size - 1) == '#')
                    ctx.fillRect((tile.size - 1) * ts, y * ts, ts, ts)
            }
            (0 until tile.size).forEach { x ->
                if (tile.at(0, x) == '#')
                    ctx.fillRect(x * ts, 0 * ts, ts, ts)
                if (tile.at(tile.size - 1, x) == '#')
                    ctx.fillRect(x * ts, (tile.size - 1) * ts, ts, ts)
            }


            // go back to original transform
            ctx.setTransform(oldTransform)
        }

    }.also {
        shouldUpdate = false
    }

    private fun setTileTransform(ctx: CanvasRenderingContext2D, tileInfo: TileInfo, tileScale: Double = 200.0) {
        ctx.translate(tileInfo.row * tileScale + tileScale/2, tileInfo.column * tileScale + tileScale/2)
        ctx.scale(0.8, 0.8)
        ctx.rotate(tileInfo.angle)
        ctx.translate(-tileScale/2, -tileScale/2.3)
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
