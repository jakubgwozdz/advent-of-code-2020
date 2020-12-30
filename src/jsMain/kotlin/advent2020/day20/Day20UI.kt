package advent2020.day20

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.ReportField
import advent2020.ResultField
import advent2020.SimpleReportFigure
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.day20.Edge.*
import advent2020.readResourceInCurrentPackage
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.js.canvas
import kotlinx.html.js.div
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.DOMMatrix
import org.w3c.dom.DOMPoint
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.math.PI

val day20puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day20puzzleInfo = PuzzleInfo("day20", "Jurassic Jigsaw", 20, 2020, animation = true)

@JsExport
fun createUI() {

    createHeader(day20puzzleInfo, day20puzzleContext)

    day20Section {
        title = "Find monsters"
        puzzleContext = day20puzzleContext
        task = ::part2
        delay = true
        resultHeading = "Corner puzzles ids"
    }

}

data class TileInfo(
    val tile: Tile,
    var row: Double,
    var column: Double,
    var angle: Double = 0.0,
    var flip: Double = 1.0,
    var scale: Double = 1.0,
    val matches: MutableMap<Edge, Long> = mutableMapOf(),
    var correctPlace: Boolean = false
)

class Day20Section(
    genericElements: GenericTaskSectionElements,
    val canvas: HTMLCanvasElement,
    val result2Field: ResultField,
    val log: ReportField
) :
    GenericTaskSection(genericElements), Day20ProgressLogger {

    private var shouldUpdate = true
    private var scale = 0.8
    private var tileCount = 0
    private var fullImage: Tile? = null
    private var imageOrientation = Orientation(Top, false)
    private var imageFlip = 1.0
    private var imageAngle = 0.0

    var timer: Int? = window.setInterval(::flush, 16)
    private fun waitTimeForOneTile() = 5000 / (tileCount / 5 + 10)
    private fun waitTimeForOneMonster() = 10000 / (tileCount * tileCount + 2)

    private val tiles: MutableMap<Long, TileInfo> = mutableMapOf()
    private val monsters: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private val currentMonster: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private var currentMonsterPct = 0.0
    private var lastAdded: Long? = null

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        result2Field.hide()
        tiles.clear()
        monsters.clear()
        log.clear()
        log.addLines("> Throwing tiles anywhere")
        scale = 0.8
        lastAdded = null
        fullImage = null
        imageFlip = 1.0
        imageAngle = 0.0
        imageOrientation = Orientation(Top, false)
        tileCount = 0
        markToRedraw()
    }

    override suspend fun success(result: String) {
        super<GenericTaskSection>.success(result)
        tiles.values
            .filter { it.matches.size == 2 }
            .fold(1L) { acc, tileInfo -> acc * tileInfo.tile.id }
            .let { resultField.show(it.toString()) }
        log.addLines("> Found $result pixels that are not monsters")
        result2Field.show(result)
        lastAdded = null
        markToRedraw()
    }

    override suspend fun foundTile(tile: Tile) {
        val r = tileCount / 12
        val c = tileCount % 12

        val startAngle = (tile.id % 360 - 180.0) / 4
        val endAngle = tile.id / 10.0 - 300.0
        val startScale = 5.0
        val endScale = 1.0
        val startFlip = 1.0
        val endFlip = 1.0
        val startRow = r + 5.0
        val endRow = r.toDouble()
        val startColumn = tile.id / 300.0
        val endColumn = c.toDouble()

        val tileInfo = TileInfo(tile, startRow, startColumn, startAngle, startFlip, startScale)

        this.tiles[tile.id] = tileInfo
        lastAdded = tile.id
        tileCount++

        delayIfChecked(waitTimeForOneTile() / 2) { pct ->
            tileInfo.row = (endRow - startRow) * pct + startRow
            tileInfo.column = (endColumn - startColumn) * pct + startColumn
            tileInfo.angle = (endAngle - startAngle) * pct + startAngle
            tileInfo.flip = (endFlip - startFlip) * pct + startFlip
            tileInfo.scale = (endScale - startScale) * pct + startScale
            markToRedraw()
        }
    }

    override suspend fun allTilesFound() {
        lastAdded = null
        tileCount = 0
        log.addLines("> ${tiles.size} tiles found")
        markToRedraw()
        delayIfChecked(1000)
        log.addLines("> Searching for matches between tiles")
    }

    override suspend fun foundMatch(id1: Long, edge: Edge, id2: Long) {
        tileCount++
        tiles[id1]!!.matches += edge to id2
        if (lastAdded != id1 && tiles[lastAdded]?.matches?.size == 2) {
            tiles.values
                .filter { it.matches.size == 2 }
                .fold(1L) { acc, tileInfo -> acc * tileInfo.tile.id }
                .let { resultField.show(it.toString()) }
                .also { log.addLines("> Tile with id $lastAdded has only 2 matches, it's a corner!") }
        }
        lastAdded = id1
        markToRedraw()
        delayIfChecked(waitTimeForOneTile() / 3)
    }

    override suspend fun allMatchesFound() {
        lastAdded = null
        tileCount = 0
        log.addLines("> All matches found")
        markToRedraw()
        delayIfChecked(1000)
        log.addLines("> Joining tiles together")
    }


    override suspend fun tilePlaced(id: Long, row: Int, col: Int) {
        lastAdded = id
        tileCount++

        val newRow = row.toDouble()
        val newColumn = col.toDouble()
        val prevTile = tiles.values.singleOrNull { it.row == newRow && it.column == newColumn }
        val tileInfo = tiles[id]!!
        val oldRow = tileInfo.row
        val oldColumn = tileInfo.column

        delayIfChecked(waitTimeForOneTile()) { pct ->
            tileInfo.row = (newRow - oldRow) * pct + oldRow
            tileInfo.column = (newColumn - oldColumn) * pct + oldColumn
            prevTile?.row = (oldRow - newRow) * pct + newRow
            prevTile?.column = (oldColumn - newColumn) * pct + newColumn
            markToRedraw()
        }
        tileInfo.correctPlace = true
    }

    override suspend fun allTilesPlaced() {
        lastAdded = null
        tileCount = 0
        log.addLines("> All tiles joined")
        markToRedraw()
        delayIfChecked(1000)
        log.addLines("> Removing border on each tile as it's not part of image")
    }

    override suspend fun tileOriented(id: Long, orientation: Orientation) {
        tileCount++
        lastAdded = id
        val newAngle = when (orientation.topEdge) {
            Top -> 0.0
            Right -> -90.0
            Bottom -> 180.0
            Left -> 90.0
        }
        val newFlip = if (orientation.flip) -1.0 else 1.0
        val tileInfo = tiles[id]!!
        val oldAngle = tileInfo.angle
        val oldFlip = tileInfo.flip
        delayIfChecked(waitTimeForOneTile()) { pct ->
            tileInfo.angle = (newAngle - oldAngle) * pct + oldAngle
            tileInfo.flip = (newFlip - oldFlip) * pct + oldFlip
            markToRedraw()
        }
    }

    override suspend fun monsterFound(y: Int, x: Int, o: Orientation, pixels: List<Pair<Int, Int>>) {
        tileCount++

        log.addLines("> Found monster: row $y column ${x + 20}")
        delayIfChecked(waitTimeForOneMonster()) { pct ->
            currentMonster.addAll(pixels)
            currentMonsterPct = pct
            markToRedraw()
        }
        currentMonster.clear()
        monsters.addAll(pixels)

        if (o != imageOrientation) {
            val oldAngle = imageAngle
            val oldFlip = imageFlip
            val newAngle = when (o.topEdge) {
                Top -> 0.0
                Right -> -90.0
                Bottom -> 180.0
                Left -> 90.0
            }
            val newFlip = if (o.flip) -1.0 else 1.0
            delayIfChecked(1500) { pct ->
                imageAngle = (newAngle - oldAngle) * (pct * pct) + oldAngle
                imageFlip = (newFlip - oldFlip) * (1 - (1 - pct) * (1 - pct)) + oldFlip
                markToRedraw()
            }
            imageOrientation = o
        } else {
            markToRedraw()
        }
    }

    override suspend fun imageComposed(image: Tile) {
        lastAdded = null
        tileCount = 0
        delayIfChecked(1200) { pct ->
            this.scale = (1.0 - 0.8) * pct + 0.8
            markToRedraw()
        }

        delayIfChecked(1500)
        delayIfChecked(1200) { pct ->
            this.scale = (1.25 - 1.0) * pct + 1.0
            markToRedraw()
        }
        fullImage = image
        log.addLines("> Full image composed, looking for monsters. Monsters look like this:")
        log.addLines(*monster.lines().toTypedArray())
        delayCheckbox.state = true
        markToRedraw()
    }

    object Colors {
        const val border = "rgb(200,200,255)"
        const val background = "rgb(75, 101, 171)"
        const val tileBg = "rgb(34, 85, 221)"
        const val text = "rgb(255, 255, 255)"
        const val imagePixel = "rgb(221, 238, 255)"
        const val monsterPixel = "rgb(255, 0, 0)"
        const val jigsawPixel = "rgb(255, 176, 121)"
        const val jigsawPixelNoMatch = "rgb(0, 0, 0)"
        const val connection = "rgb(255, 0, 0)"
    }

    fun markToRedraw() {
        shouldUpdate = true
    }

    fun fitToParent() {
        log as SimpleReportFigure
        val w = log.wholeElement.clientWidth
        console.log("setting canvas size to $w")
        canvas.width = w
        canvas.height = w
        log.preElement.style.removeProperty("max-height")
        val titleHeight = log.titleElement.parentElement!!.clientHeight
        console.log("title height is $titleHeight px")
        val preHeight = w - titleHeight
        log.preElement.style.height = "${preHeight}px"
        console.log("setting log height to $preHeight px")
        markToRedraw()
    }

    init {
        log.show("")
        fitToParent()
    }

    private fun flush() {
        if (shouldUpdate) canvas.getContext("2d").let { ctx ->
            ctx as CanvasRenderingContext2D
            val w = ctx.canvas.width
            ctx.save()
            ctx.scale(w / 2400.0, w / 2400.0)

            ctx.fillStyle = Colors.border
            ctx.fillRect(0.0, 0.0, 2400.0, 2400.0)
            ctx.fillStyle = Colors.background
            ctx.fillRect(4.0, 4.0, 2392.0, 2392.0)

            val image = fullImage
            if (image != null) {
                ctx.save()
                val ts = 2400.0 / image.lines.size

                val transform = DOMMatrix()
                    .translate(2400.0 / 2, 2400.0 / 2)
                    .rotate(imageAngle)
                    .multiply(DOMMatrix(arrayOf(imageFlip, 0.0, 0.0, 1.0, 0.0, 0.0)))
                    .translate(-2400.0 / 2, -2400.0 / 2)

                ctx.setTransform(ctx.getTransform().multiply(transform))

                ctx.fillStyle = Colors.imagePixel
                image.lines.indices.forEach { y ->
                    image.lines[y].indices.forEach { x ->
                        if (image.lines[y][x] == '#') {
                            ctx.fillRect(x * ts, y * ts, ts, ts)
                        }
                    }
                }

                ctx.save()
                ctx.fillStyle = Colors.monsterPixel
                monsters.forEach { (y, x) ->
                    ctx.fillRect(x * ts - 10, y * ts - 10, ts + 20, ts + 20)
                }

                ctx.globalAlpha = 0.5
                ctx.fillStyle = Colors.imagePixel
                monsters.forEach { (y, x) ->
                    ctx.fillRect(x * ts, y * ts, ts, ts)
                }
                ctx.restore()

                ctx.save()
                ctx.globalAlpha = currentMonsterPct
                ctx.fillStyle = Colors.monsterPixel
                currentMonster.forEach { (y, x) ->
                    ctx.fillRect(x * ts - 10, y * ts - 10, ts + 20, ts + 20)
                }
                ctx.globalAlpha = 1 - (currentMonsterPct / 2)
                ctx.fillStyle = Colors.imagePixel
                currentMonster.forEach { (y, x) ->
                    ctx.fillRect(x * ts, y * ts, ts, ts)
                }
                ctx.restore()



                ctx.restore()
            } else {

                // connections
                tiles.filter { (_, tileInfo) -> !tileInfo.correctPlace }
                    .forEach { (_, tileInfo) ->
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

            }
            ctx.restore()
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

        ctx.globalAlpha = aBg * (1.0 / tileInfo.scale)
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

        ctx.globalAlpha = aFg * (1.0 / tileInfo.scale)
        ctx.fillStyle = Colors.imagePixel
        (1..tile.size - 2).forEach { y ->
            (1..tile.size - 2).forEach { x ->
                if (tile.at(y, x) == '#')
                    ctx.fillRect(x * ts, y * ts, ts, ts)
            }
        }

        ctx.globalAlpha = aBg * (1.0 / tileInfo.scale)
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
        .translate(tileInfo.column * size, tileInfo.row * size)
        .translate(size / 2, size / 2)
        .scale(scale)
        .rotate(tileInfo.angle)
        .multiply(DOMMatrix(arrayOf(tileInfo.flip, 0.0, 0.0, 1.0, 0.0, 0.0)))
        .scale(tileInfo.scale)
        .translate(-size / 2, -size / 2)

}

class Day20Part1SectionBuilder : TaskSectionBuilder() {
    lateinit var canvas: HTMLCanvasElement
    var result2Heading = "Non-monster pixels"
    lateinit var result2Field: ResultField
    lateinit var log: ReportField

    override fun createTaskSpecificLevelFields(bodyBuilder: TagConsumer<HTMLElement>) {
        result2Field = bodyBuilder.createResultField(result2Heading)
    }

    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) {
        with(bodyBuilder) {
            div("columns") {
                div("column is-half") {
                    canvas = canvas { }
                }
                div("column is-half") {
                    log = bodyBuilder.createLogField()
                }
            }
        }
    }

    override fun constructObject() = Day20Section(genericElements(), canvas, result2Field, log)
        .apply { window.onresize = { this.fitToParent() } }
}

fun day20Section(op: Day20Part1SectionBuilder.() -> Unit) = Day20Part1SectionBuilder()
    .apply(op)
    .buildInBody(document.body!!)
