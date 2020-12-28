package advent2020.day20

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.day20.Edge.*
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
import kotlin.math.PI

val day20puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day20puzzleInfo = PuzzleInfo("day20", "Jurassic Jigsaw", 20, 2020)

@JsExport
fun createUI() {

    createHeader(day20puzzleInfo, day20puzzleContext)

    day20Section {
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
        markToRedraw()
    }

    override suspend fun success(result: String) {
        super<GenericTaskSection>.success(result)
        lastAdded = null
        markToRedraw()
    }

    override suspend fun foundTiles(tiles: Map<Long, Tile>) {
        var index = 0
        tiles.forEach { (id, tile) ->
            val r = index / 12
            val c = index % 12
            index++
            this.tiles[tile.id] = TileInfo(tile, r, c)
        }
        markToRedraw()
        delayIfChecked(16)
    }

    override suspend fun foundMatch(id1: Long, edge: Edge, id2: Long) {
        tiles[id1] = tiles[id1]!!.run { copy(matches = matches + (edge to id2)) }
        lastAdded = tiles[id1]
        markToRedraw()
        delayIfChecked(16)
    }

    object Colors {
        const val border = "rgb(200,200,255)"
        const val background = "rgb(75, 101, 171)"
        const val tileBg = "rgb(34, 85, 221)"
        const val text = "rgb(255, 255, 255)"
        const val imagePixel = "rgb(221, 238, 255)"
        const val jigsawPixel = "rgb(91, 176, 121)"
        const val jigsawPixelNoMatch = "rgb(0, 0, 0)"
        const val connection = "rgb(255, 0, 0)"
    }

    var lines = 0

    fun markToRedraw() {
        shouldUpdate = true
    }

    private fun flush() {
        if (shouldUpdate) canvas.getContext("2d").let { ctx ->
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
                val highlighted = tileInfo == lastAdded

                drawConnections(ctx, tileInfo, if (highlighted) 1.0 else 0.2)

                // go back to original values
                ctx.restore()
            }

            // tiles
            tiles.forEach { (_, tileInfo) ->
                ctx.save()
                val highlighted =
                    tileInfo == lastAdded || lastAdded?.matches?.values?.contains(tileInfo.tile.id) == true || tileInfo.matches.count() == 2

                drawTile(ctx, tileInfo, if (highlighted) 0.9 else 0.3)

                // go back to original values
                ctx.restore()
            }

            ctx.globalAlpha = 1.0

        }.also {
            shouldUpdate = false
        }
    }

    private fun drawConnections(ctx: CanvasRenderingContext2D, tileInfo: TileInfo, alpha: Double) {
        ctx.globalAlpha = alpha
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
    }

    val borderPixelsCache = mutableMapOf<Int, Collection<Pair<Int, Int>>>()
    fun borderPixels(size: Int): Collection<Pair<Int, Int>> {
        return borderPixelsCache.getOrPut(size) {
            (0 until size)
                .flatMap { listOf(0 to it, size - 1 to it, it to 0, it to size - 1) }
                .toSet()
        }
    }

    private fun drawTile(ctx: CanvasRenderingContext2D, tileInfo: TileInfo, alpha: Double) {
        ctx.setTransform(ctx.getTransform().multiply(tileTransform(tileInfo)))

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
        ctx.fillText("${tile.id}", 10.0, -5.0)

        ctx.fillStyle = Colors.imagePixel
        (1..tile.size - 2).forEach { y ->
            (1..tile.size - 2).forEach { x ->
                if (tile.at(y, x) == '#')
                    ctx.fillRect(x * ts, y * ts, ts, ts)
            }
        }
        // edges
        val hasT = top in tileInfo.matches
        val hasR = right in tileInfo.matches
        val hasB = bottom in tileInfo.matches
        val hasL = left in tileInfo.matches

        borderPixels(tile.size)
            .filter { (x, y) -> tile.at(y, x) == '#' }
            .forEach { (x, y) ->
                if (x == 0 && hasL || x == tile.size - 1 && hasR || y == 0 && hasT || y == tile.size - 1 && hasB)
                    ctx.fillStyle = Colors.jigsawPixel
                else
                    ctx.fillStyle = Colors.jigsawPixelNoMatch
                ctx.fillRect(x * ts, y * ts, ts, ts)
            }


        ctx.fillStyle = Colors.text
        ctx.font = "24px Lato"
        tileInfo.matches.forEach { (edge, id) ->
            ctx.save()
            ctx.translate(100.0, 100.0)
            when (edge) {
                top -> Unit
                right -> ctx.rotate(PI / 2)
                bottom -> ctx.rotate(PI)
                left -> ctx.rotate(3 * PI / 2)
            }
            val v = ctx.measureText("$id").width
            ctx.fillText("$id", -v / 2, -80.0)

            ctx.restore()
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
        .apply { window.onresize = { this.markToRedraw() } }
}

fun day20Section(op: Day20Part1SectionBuilder.() -> Unit) = Day20Part1SectionBuilder().apply(op)
