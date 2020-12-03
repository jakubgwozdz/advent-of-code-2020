package advent2020.day03

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.ResultField
import advent2020.TaskSection
import advent2020.TaskSectionBuilder
import advent2020.createHeader
import advent2020.createInputSectionWithModal
import advent2020.fancyShadow
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.Entities.nbsp
import kotlinx.html.TagConsumer
import kotlinx.html.js.article
import kotlinx.html.js.div
import kotlinx.html.js.p
import kotlinx.html.style
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement

val day03puzzleContext by lazy { PuzzleContext(day03myPuzzleInput) }
val day03puzzleInfo = PuzzleInfo("day03", "Toboggan Trajectory", 3, 2020)

@JsExport
fun createUI() {

    createHeader(day03puzzleInfo)
    createInputSectionWithModal(day03puzzleInfo, day03puzzleContext, readOnly = true)

    Day03SectionBuilder().buildInBody(document.body!!)

}

internal class Day03Section(
    genericElements: GenericTaskSectionElements,
    val result2Field: ResultField,
    val slopeTiles: Map<Vector, SlopeField>,
) : GenericTaskSection(genericElements), DayO3ProgressReceiver {

    override suspend fun success(result: String) {
        console.log(result)
        result2Field.show(result)
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-invisible")
        progressField.success()
    }

    override suspend fun totalCollisions(count: Long, move: Vector) {
        console.log("totalCollisions($count, $move)")
        if (move == part1move) {
            resultField.show(count.toString())
        }
    }

    override suspend fun reset(lines: List<String>, move: Vector) {
        slopeTiles[move]?.reset(lines)
    }

    override suspend fun moveTo(x: Int, y: Int, move: Vector) {
        slopeTiles[move]?.moveTo(x, y)
        delayIfChecked(10)
    }

}

interface SlopeField {
    fun reset(lines: List<String>)
    fun markCollision(x: Int, y: Int)
    fun moveTo(x: Int, y: Int)
}

typealias Position = Pair<Int, Int>

internal class SlopeTile(val box: HTMLDivElement, val collisionsP: HTMLParagraphElement) : SlopeField {

    var currentPos = 0 to 0
    val trees = mutableSetOf<Position>()
    val collisions = mutableSetOf<Position>()
    var period = Int.MAX_VALUE

    override fun reset(lines: List<String>) {
        currentPos = 0 to 0
        trees.clear()
        collisions.clear()
        period = lines.map { it.length }.distinct().single()

        lines.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                if (c == '#') {
                    trees += x to y
                }
            }
        }

    }

    override fun markCollision(x: Int, y: Int) {
        collisions.add(x % period to y)
        console.log("collision at ${x to y}")
        collisionsP.textContent = "Collisions: ${collisions.size}"
    }

    override fun moveTo(x: Int, y: Int) {
        currentPos = x to y
        val wrapped = x % period to y
        console.log("move to ${x to y}")
        if (trees.contains(wrapped)) markCollision(x, y)
    }

}

internal class Day03SectionBuilder : TaskSectionBuilder() {

    init {
        title = "Part 1 & 2"
        puzzleContext = day03puzzleContext
        task = ::part2
        delay = true
        resultHeading = "Part 1"
    }

    override fun createTaskSpecificLevelFields(div: TagConsumer<HTMLElement>) {
        result2Field = div.createResultField("Part 2")
    }

    lateinit var result2Field: ResultField
    lateinit var slopeTiles: Map<Vector, SlopeField>

    override fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {
        with(div) {
            style {
                unsafe { //language=CSS
                    raw(".day03tree { position:absolute; }")
                }
            }

            slopeTiles = moves.chunked(3).flatMap { chunk ->
                val tiles = mutableListOf<Pair<Vector, SlopeField>>()
                div("tile is-ancestor") {
                    tiles += chunk.map { vector ->
                        vector to createSlopeTile(vector)
                    }
                }
                tiles
            }.toMap()

        }
    }

    protected fun TagConsumer<HTMLElement>.createSlopeTile(vector: Vector): SlopeField {
        lateinit var box: HTMLDivElement
        lateinit var collisionsP: HTMLParagraphElement
        div("tile is-parent is-4") {
            article("tile is-child box") {
                style = fancyShadow
                div("container") {
                    div { p("heading") { +"Slope: right ${vector.first}; down ${vector.second}" } }
                }


                box = div("box") {
                    style = "background-color: #000; position:relative; overflow:hidden; height:20rem"
//                    day03myPuzzleInput.trim().lines().forEachIndexed { y, l ->
//                        l.forEachIndexed { x, c ->
//                            if (c == '#') {
//                                div("day03tree") {
//                                    +if (y * vector.first % l.count() == (x * vector.second)) "\uD83D\uDCA5" else "\uD83C\uDF32" //else "\uD83E\uDDDD"//"\uD83D\uDEF7\u200D\u27A1\uFE0F" //
//                                    style = "top: ${y}em; left:${x}em;"
//                                }
//                            }
//                        }
//                    }
                }
                div("container") {
                    div { collisionsP = p("heading") { +nbsp } }
                }
            }
        }
        return SlopeTile(box, collisionsP)
    }

    override fun constructObject(): TaskSection {
        return Day03Section(
            genericElements(),
            result2Field,
            slopeTiles
        )
    }
}
