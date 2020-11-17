package advent2020.day00

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.TaskLauncher
import advent2020.createHeader
import advent2020.day00.Day00HtmlReporter.Current.NONE
import advent2020.day00.Day00HtmlReporter.Current.PART1
import advent2020.day00.Day00HtmlReporter.Current.PART2
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.button
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.progress
import kotlinx.html.js.section
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLProgressElement

val puzzleContext by lazy { PuzzleContext(myPuzzleInput, ::part1) }
val puzzleInfo = PuzzleInfo("day00", "The Tyranny of the Rocket Equation (from 2019)", 1, 2019)

@JsExport
fun createUI() {

    val progressReporter = Day00HtmlReporter()

    createHeader(puzzleInfo, puzzleContext)
    progressReporter.appendTo(document.body!!)
}


class Day00HtmlReporter : Day00ProgressReporter {

    enum class Current { NONE, PART1, PART2 }

    val taskLauncher = TaskLauncher(puzzleContext, this)

    var currentTask = NONE

    override suspend fun startingPart1() {
        part01Loading.addClass("is-loading")
        part01Progress.removeClass("is-error")
        part01Progress.removeClass("is-success")
        part01Progress.attributes.removeNamedItem("value")
        currentTask = PART1
    }

    override suspend fun startingPart2() {
        part01Loading.removeClass("is-loading")
        currentTask = PART2
    }

    override suspend fun phaseFinished(result: String) {
        part01Loading.removeClass("is-loading")
        if (currentTask == PART1) {
            part01Progress.addClass("is-success")
            part01Progress.value = part01Progress.max
        }
        currentTask = NONE
    }

    override suspend fun error(message: Any?) {
        part01Loading.removeClass("is-loading")
        if (currentTask == PART1) {
            part01Progress.addClass("is-error")
            part01Progress.value = part01Progress.max
        }
        currentTask = NONE
    }

    override suspend fun reportPart1Progress(no: Int, total: Int, mass: Long, fuel: Long, sum: Long) {
        part01Progress.apply { value = no.toDouble(); max = total.toDouble() }
        console.log("$no/$total: mass=$mass => fuel=$fuel, sum=$sum")
    }


    override val delay: Long
        get() = 13

    fun appendTo(body: HTMLElement) {
        body.append { createPart01() }
    }

    private fun TagConsumer<HTMLElement>.createPart01() {
        section("section") {
            div("container") {
                div("level") {
                    div("level-left") {
                        div("level-item") {
                            h1("title") { +"Part 1" }
                        }
                        div("level-item") {
                            div("control") {
                                part01Loading = button(classes = "button") {
                                    +"Run"
                                    onClickFunction = { taskLauncher.launchPart1() }
                                }
                            }
                        }
                    }
                }
                part01Progress = progress("progress has-background-grey-dark is-info") {
                    value = "0"
                    max = "100"
                }
            }
        }
    }

    private lateinit var part01Progress: HTMLProgressElement
    private lateinit var part01Loading: HTMLElement
}
