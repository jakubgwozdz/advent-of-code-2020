package advent2020

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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

open class TaskSection(
    private val title: String,
    private val puzzleContext: PuzzleContext,
    private val task: PuzzleTask = { _, _ -> TODO("Part not yet implemented") }
) : ProgressReceiver {

    override suspend fun starting() {
        launchButton.addClass("is-loading")
        cancelButton.removeClass("is-hidden")
        progressBar.removeClass("is-danger")
        progressBar.removeClass("is-success")
        progressBar.attributes.removeNamedItem("value")
    }

    override suspend fun success(result: String) {
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-hidden")
        progressBar.addClass("is-success")
        progressBar.value = progressBar.max
        console.log(result)
    }

    override suspend fun error(message: Any?) {
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-hidden")
        progressBar.addClass("is-danger")
        progressBar.value = progressBar.max
        console.log(message)
    }

    fun appendTo(body: HTMLElement) {
        body.append { createSection() }
    }

    private fun TagConsumer<HTMLElement>.createSection() {
        section("section") {
            div("container") {
                div("level") {
                    div("level-left") {
                        div("level-item") {
                            h1("title") { +title }
                        }
                        div("level-item") {
                            div("control") {
                                launchButton = button(classes = "button") {
                                    +"Run"
                                    onClickFunction = { launch() }
                                }
                            }
                            div("control") {
                                cancelButton = button(classes = "delete is-hidden") {
                                    onClickFunction = { cancel() }
                                }
                            }
                        }
                    }
                }
                progressBar = progress("progress has-background-grey-dark is-info") {
                    value = "0"
                    max = "100"
                }
            }
        }
    }

    var activeJob: Job? = null

    fun launch() {
        activeJob?.let { if (it.isActive) it.cancel("cancelling because rerun") }
        activeJob = GlobalScope.launch {
            starting()
            try {
                val result = task(puzzleContext.input, this@TaskSection)
                success(result)
            } catch (e: Throwable) {
                error(e)
            }
        }
    }

    fun cancel() {
        activeJob?.let { if (it.isActive) it.cancel("cancelling") }
    }

    lateinit var progressBar: HTMLProgressElement
    lateinit var launchButton: HTMLElement
    lateinit var cancelButton: HTMLElement


}
