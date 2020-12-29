package advent2020.day20

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.ResultField
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.day20.Edge.*
import advent2020.readResourceInCurrentPackage
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
        title = "Part 1 & 2"
        puzzleContext = day20puzzleContext
        task = ::part2
        delay = true
        resultHeading = "Part 1"
    }

}

data class TileInfo(
    val tile: Tile,
    val row: Double,
    val column: Double,
    val angle: Double = (tile.id % 360 - 180.0) / 4,// * PI / 180,
    val flip: Double = 1.0,
    val matches: Map<Edge, Long> = emptyMap(),
    val correctPlace: Boolean = false
)

class Day20Section(
    genericElements: GenericTaskSectionElements,
    val canvas: HTMLCanvasElement,
    val result2Field: ResultField
) :
    GenericTaskSection(genericElements), Day20ProgressLogger {

    private var shouldUpdate = false
    private var scale = 0.8

    var timer: Int? = window.setInterval(::flush, 16)

    private val tiles: MutableMap<Long, TileInfo> = mutableMapOf()
    private var lastAdded: Long? = null

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        result2Field.hide()
        tiles.clear()
        scale = 0.8
        lastAdded = null
        markToRedraw()
    }

    override suspend fun success(result: String) {
        super<GenericTaskSection>.success(result)
        tiles.values
            .filter { it.matches.size == 2 }
            .fold(1L) { acc, tileInfo -> acc * tileInfo.tile.id }
            .let { resultField.show(it.toString()) }
        result2Field.show(result)
        lastAdded = null
        markToRedraw()
    }

    override suspend fun foundTile(tile: Tile) {
        val index = tiles.size
        val r = index / 12
        val c = index % 12
        this.tiles[tile.id] = TileInfo(tile, r.toDouble(), c.toDouble())
        lastAdded = tile.id
        markToRedraw()
        delayIfChecked(15)
    }

    override suspend fun allTilesFound() {
        lastAdded = null
        markToRedraw()
        delayIfChecked(1000)
    }

    override suspend fun foundMatch(id1: Long, edge: Edge, id2: Long) {
        val updated = tiles[id1]!!.run { copy(matches = matches + (edge to id2)) }
        tiles[id1] = updated
        if (lastAdded != id1 && tiles[lastAdded]?.matches?.size == 2) {
            tiles.values
                .filter { it.matches.size == 2 }
                .fold(1L) { acc, tileInfo -> acc * tileInfo.tile.id }
                .let { resultField.show(it.toString()) }
        }
        lastAdded = id1
        markToRedraw()
        delayIfChecked(15)
    }

    override suspend fun allMatchesFound() {
        lastAdded = null
        markToRedraw()
        delayIfChecked(1000)
    }


    override suspend fun tilePlaced(id: Long, row: Int, col: Int) {
        val oldInfo = tiles[id]!!
        tiles[id] = oldInfo.copy(row = row.toDouble(), column = col.toDouble(), correctPlace = true)
        lastAdded = id
        markToRedraw()
        delayIfChecked(15)
    }

    override suspend fun allTilesPlaced() {
        lastAdded = null
        markToRedraw()
        delayIfChecked(1000)
    }

    override suspend fun tileRotated(id: Long, orientation: Orientation) {
        val angle = when (orientation.topEdge) {
            Top -> 0.0
            Right -> -90.0
            Bottom -> 180.0
            Left -> 90.0
        }
        tiles[id] = tiles[id]!!.copy(angle = angle, flip = if (orientation.flip) -1.0 else 1.0)
        lastAdded = id
        markToRedraw()
        delayIfChecked(15)
    }

    override suspend fun allTilesRotated() {
        lastAdded = null
        (800..1000).forEach { scale ->
            this.scale = scale / 1000.0
            markToRedraw()
            delayIfChecked(6)
        }
        delayIfChecked(1500)
        (1000..1250).forEach { scale ->
            this.scale = scale / 1000.0
            markToRedraw()
            delayIfChecked(6)
        }
    }


    object Colors {
        const val border = "rgb(200,200,255)"
        const val background = "rgb(75, 101, 171)"
        const val tileBg = "rgb(34, 85, 221)"
        const val text = "rgb(255, 255, 255)"
        const val imagePixel = "rgb(221, 238, 255)"
        const val jigsawPixel = "rgb(255, 176, 121)"
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

            // connections
            tiles.filter { (_, tileInfo) -> !tileInfo.correctPlace }.forEach { (_, tileInfo) ->
                ctx.save()
                val highlighted = tileInfo.tile.id == lastAdded

                drawConnections(ctx, tileInfo, if (highlighted) 1.0 else 0.2)

                // go back to original values
                ctx.restore()
            }

            // tiles
            tiles.forEach { (id, tileInfo) ->
                ctx.save()
                val alpha = when {
                    id == lastAdded -> 1.0
                    tiles[lastAdded]?.matches?.values?.contains(id) == true -> 1.0
                    tileInfo.matches.count() == 2 -> 0.9
                    tileInfo.correctPlace -> 0.8
                    else -> 0.3
                }
                drawTile(ctx, tileInfo, alpha)

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

        val aFg = (1.0 - alpha) * (scale - 0.8) / (0.45) + alpha
        val aBg = (-alpha) * (scale - 0.8) / (0.45) + alpha

        ctx.globalAlpha = aBg
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

        ctx.globalAlpha = aFg
        ctx.fillStyle = Colors.imagePixel
        (1..tile.size - 2).forEach { y ->
            (1..tile.size - 2).forEach { x ->
                if (tile.at(y, x) == '#')
                    ctx.fillRect(x * ts, y * ts, ts, ts)
            }
        }

        ctx.globalAlpha = aBg
        // edges
        val hasT = Top in tileInfo.matches
        val hasR = Right in tileInfo.matches
        val hasB = Bottom in tileInfo.matches
        val hasL = Left in tileInfo.matches

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
                Top -> Unit
                Right -> ctx.rotate(PI / 2)
                Bottom -> ctx.rotate(PI)
                Left -> ctx.rotate(3 * PI / 2)
            }
            val v = ctx.measureText("$id").width
            ctx.fillText("$id", -v / 2, -80.0)

            ctx.restore()
        }
    }

    private fun tileTransform(tileInfo: TileInfo, size: Double = 200.0) = DOMMatrix()
        .translate(tileInfo.column.toDouble() * size, tileInfo.row.toDouble() * size)
        .translate(size / 2, size / 2)
        .scale(scale)
        .rotate(tileInfo.angle)
//        .run { if (tileInfo.flip) flipX() else this }
        .scaleNonUniform(tileInfo.flip)
        .translate(-size / 2, -size / 2)

}

class Day20Part1SectionBuilder : TaskSectionBuilder() {
    lateinit var canvas: HTMLCanvasElement
    var result2Heading = "Part 2"
    lateinit var result2Field: ResultField

    override fun createTaskSpecificLevelFields(bodyBuilder: TagConsumer<HTMLElement>) {
        result2Field = bodyBuilder.createResultField(result2Heading)
    }

    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) {
        with(bodyBuilder) {
            canvas = canvas { }
        }
    }

    override fun constructObject() = Day20Section(genericElements(), canvas, result2Field)
        .apply { window.onresize = { this.markToRedraw() } }
}

fun day20Section(op: Day20Part1SectionBuilder.() -> Unit) = Day20Part1SectionBuilder()
    .apply(op)
    .buildInBody(document.body!!)
