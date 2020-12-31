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
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.time.Duration
import kotlin.time.TimeSource.Monotonic

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
    val canvas: Day20CanvasField,
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

    private var frameCount = 0
    private var timeSpentDrawing = Duration.ZERO
    private var startTime = Monotonic.markNow()

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

        frameCount = 0
        timeSpentDrawing = Duration.ZERO
        startTime = Monotonic.markNow()

        markToRedraw()
    }

    override suspend fun success(result: String) {
        super<GenericTaskSection>.success(result)
        tiles.values
            .filter { it.matches.size == 2 }
            .fold(1L) { acc, tileInfo -> acc * tileInfo.tile.id }
            .let { resultField.show(it.toString()) }
        log.addLines("> Found $result pixels that are not monsters")
        log.addLines("> Animation took ${startTime.elapsedNow()}, $frameCount frames was drawn, drawing took $timeSpentDrawing")
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
        val corners = tiles.values
            .filter { it.matches.size == 2 }
            .map { it.tile.id }
        val part1 = corners
            .fold(1L) { acc, id -> acc * id }
        log.addLines("> Part 1 result is ${corners.joinToString("*")} = $part1")
        resultField.show(part1.toString())


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

        delayIfChecked(1500) { pct ->
            this.scale = (1.25 - 1.0) * pct + 1.0
            markToRedraw()
        }
        fullImage = image
        log.addLines("> Full image composed, looking for monsters. Monsters look like this:")
        log.addLines(*monster.lines().toTypedArray())
        markToRedraw()
    }

    fun markToRedraw() {
        shouldUpdate = true
    }

    fun fitToParent() {
        log as SimpleReportFigure
        val w = log.wholeElement.clientWidth
        console.log("setting canvas size to $w")
        canvas.size = w
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
        val image = fullImage
        if (shouldUpdate) {
            frameCount++
            val timer = Monotonic.markNow()
            shouldUpdate = false
            if (image == null) canvas.drawTiles(
                tiles,
                lastAdded?.let { listOf(it) } ?: emptyList(),
                scale)
            if (image != null) canvas.drawImage(
                image,
                imageAngle,
                imageFlip,
                monsters,
                currentMonster,
                currentMonsterPct
            )
            timeSpentDrawing += timer.elapsedNow()
        }
    }
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

    override fun constructObject() = Day20Section(genericElements(), Day20CanvasField(canvas), result2Field, log)
        .apply { window.onresize = { this.fitToParent() } }
}

fun day20Section(op: Day20Part1SectionBuilder.() -> Unit) = Day20Part1SectionBuilder()
    .apply(op)
    .buildInBody(document.body!!)
