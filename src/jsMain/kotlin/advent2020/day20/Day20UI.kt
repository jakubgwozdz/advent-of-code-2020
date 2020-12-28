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
import org.w3c.dom.DOMMatrix
import org.w3c.dom.DOMPoint
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
        task = ::part2
    }.buildInBody(document.body!!)

}

data class TileInfo(
    val tile: Tile,
    val row: Int,
    val column: Int,
    val angle: Double = (tile.id % 360 - 180.0),// * PI / 180,
    val scale: Double = 0.8,
    val flip: Boolean = false,//tile.id>1500,
    val matches: Map<Edge, Long> = emptyMap()
)

class Day20Part1Section(genericElements: GenericTaskSectionElements, val canvas: HTMLCanvasElement) :
    GenericTaskSection(genericElements), Day20ProgressLogger {

    private var shouldUpdate = false
    var timer: Int? = window.setInterval(::flush, 16)

    private val tiles: MutableMap<Long, TileInfo> = mutableMapOf()
    private var lastAdded: TileInfo? = null

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        tiles.clear()
        shouldUpdate = true
    }

    override suspend fun success(result: String) {
        super<GenericTaskSection>.success(result)
        lastAdded = null
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
        lastAdded = tiles[id1]
        shouldUpdate = true
        delayIfChecked(16)
    }

    object Colors {
        const val border = "rgb(200,200,255)"
        const val background = "rgb(75, 101, 171)"
        const val tileBg = "rgb(34, 85, 221)"
        const val text = "rgb(255,255,255)"
        const val imagePixel = "rgb(221, 238, 255)"
        const val jigsawPixel = "rgb(91, 176, 121)"
        const val connection = "rgb(255,0,0)"
    }

    var lines = 0

    private fun flush() = canvas.getContext("2d").let { ctx ->
        ctx as CanvasRenderingContext2D
        val w = ctx.canvas.parentElement?.clientWidth!!
        ctx.canvas.width = w
        ctx.canvas.height = w
        ctx.scale(w / 2400.0, w / 2400.0)

        ctx.fillStyle = Colors.border
        ctx.fillRect(0.0, 0.0, 2400.0, 2400.0)
        ctx.fillStyle = Colors.background
        ctx.fillRect(4.0, 4.0, 2392.0, 2392.0)

        val mainTransform = ctx.getTransform()

        // connections
        tiles.forEach { (_, tileInfo) ->
            ctx.save()

            if (tileInfo != lastAdded) {
                ctx.globalAlpha = 0.2
            }

            ctx.strokeStyle = Colors.connection
            ctx.lineWidth = 5.0
            val tr1 = tileTransform(tileInfo)
            tileInfo.matches.forEach { (e, id) ->
                val other = tiles[id]!!
                val tr2 = tileTransform(other)
                val start = DOMPoint(100.0, 100.0).matrixTransform(tr1)
                val end = DOMPoint(100.0, 100.0).matrixTransform(tr2)
                ctx.beginPath()
                ctx.moveTo(start.x, start.y)
                ctx.lineTo(end.x, end.y)
                ctx.stroke()
                if (lines < 100) console.log("${tileInfo.tile.id} [${start.x}, ${start.y}] - ${other.tile.id} [${end.x}, ${end.y}] ")
                lines++
            }


            // go back to original values
            ctx.restore()
        }

        // tiles
        tiles.forEach { (_, tileInfo) ->
            ctx.save()
            ctx.setTransform(ctx.getTransform().multiply(tileTransform(tileInfo)))
            drawTile(
                ctx,
                tileInfo,
                if (tileInfo == lastAdded || lastAdded?.matches?.values?.contains(tileInfo.tile.id) == true || tileInfo.matches.count() == 2) 0.9 else 0.3
            )
            // go back to original values
            ctx.restore()
        }

        ctx.globalAlpha = 1.0

    }.also {
        shouldUpdate = false
    }

    private fun drawTile(ctx: CanvasRenderingContext2D, tileInfo: TileInfo, alpha: Double) {
        val tile = tileInfo.tile

        val ts = 20.0 // tile size


        ctx.globalAlpha = alpha

        ctx.fillStyle = Colors.border
        ctx.fillRect(-2.0, -2.0, 204.0, 204.0)
        ctx.fillStyle = Colors.tileBg
        ctx.fillRect(0.0, 0.0, 200.0, 200.0)

        ctx.strokeStyle = Colors.border
        ctx.beginPath()
        ctx.moveTo(0.0, 0.0)
        ctx.lineTo(5.0, -30.0)
        ctx.lineTo(90.0, -30.0)
        ctx.lineTo(100.0, 0.0)
        ctx.stroke()

        ctx.fillStyle = Colors.text
        ctx.font = "30px Lato"
        ctx.scale(2.0, 1.0)
        ctx.fillText("${tile.id}", 10.0, -5.0, 30.0)
        ctx.scale(0.5, 1.0)

        ctx.fillStyle = Colors.imagePixel
        (1..tile.size - 2).forEach { y ->
            (1..tile.size - 2).forEach { x ->
                if (tile.at(y, x) == '#')
                    ctx.fillRect(x * ts, y * ts, ts, ts)
            }
        }
        // edges
        ctx.fillStyle = Colors.jigsawPixel
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
    }

    private fun tileTransform(tileInfo: TileInfo, size: Double = 200.0) = DOMMatrix()
        .translate(tileInfo.row * size + size / 2, tileInfo.column * size + size / 2)
        .scale(if (tileInfo.flip) -tileInfo.scale else tileInfo.scale, tileInfo.scale)
        .rotate(tileInfo.angle)
        .translate(-size / 2, -size / 2.3)

}

class Day20Part1SectionBuilder : TaskSectionBuilder() {
    lateinit var canvas: HTMLCanvasElement
    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) {
        with(bodyBuilder) {
            canvas = canvas { }
        }
    }

    override fun constructObject() = Day20Part1Section(genericElements(), canvas)
}

fun day20part1Section(op: Day20Part1SectionBuilder.() -> Unit) = Day20Part1SectionBuilder().apply(op)
