package advent2020

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.Entities.nbsp
import kotlinx.html.TagConsumer
import kotlinx.html.js.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLProgressElement

open class TaskSection(
    private val title: String,
    private val puzzleContext: PuzzleContext,
    private val task: PuzzleTask = { _, _ -> TODO(title) }
) : ProgressReceiver {

    override suspend fun starting() {
        console.log("Starting $title")
        launchButton.addClass("is-loading")
        cancelButton.removeClass("is-invisible")
        resultItem.addClass("is-invisible")
        errorItem.addClass("is-hidden")
        progressBar.removeClass("is-danger")
        progressBar.removeClass("is-success")
        progressBar.attributes.removeNamedItem("value")
    }

    override suspend fun success(result: String) {
        console.log(result)
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-invisible")
        progressBar.addClass("is-success")
        progressBar.value = progressBar.max
        resultItem.removeClass("is-invisible")
        resultTag.textContent = result
    }

    override suspend fun error(message: Any?) {
        console.log(message)
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-invisible")
        progressBar.addClass("is-danger")
        errorItem.removeClass("is-hidden")
        errorTag.textContent = JSON.stringify(message, null, 2)
        errorMessage.textContent = message.toString()
        progressBar.value = progressBar.max
    }

    fun appendTo(body: HTMLElement) {
        body.append { createSection() }
    }

    protected open fun TagConsumer<HTMLElement>.createSection() {
        section("section") {
            div("container") {
                div("level") {
                    div("level-left") {
                        div("level-item") {
                            p("title is-3") { +title }
                        }
                        div("level-item") {
                            div("control") {
                                launchButton = button(classes = "button") {
                                    +"Run"
                                    onClickFunction = { launch() }
                                }
                            }
                        }
                    }
                    createResultItem()
                    div("level-right") {
                        div("level-item") {
                            div("control") {
                                cancelButton = button(classes = "delete is-large is-invisible") {
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

                errorItem = figure("box is-hidden") {
                    errorMessage = p("subtitle is-5 is-failure") { }
                    errorTag = pre { }
                }
            }
        }


    }

    protected open fun TagConsumer<HTMLElement>.createResultItem() {
        resultItem = div("level-item has-text-centered is-invisible") {
            div {
                p("heading") { +"Result: " }
                p { resultTag = code { +nbsp } }
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
    lateinit var resultItem: HTMLElement
    lateinit var resultTag: HTMLElement
    lateinit var errorItem: HTMLElement
    lateinit var errorMessage: HTMLElement
    lateinit var errorTag: HTMLElement

}
