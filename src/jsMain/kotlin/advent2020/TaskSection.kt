package advent2020

import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.Entities.nbsp
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.button
import kotlinx.html.js.code
import kotlinx.html.js.div
import kotlinx.html.js.figure
import kotlinx.html.js.p
import kotlinx.html.js.pre
import kotlinx.html.js.progress
import kotlinx.html.js.section
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLProgressElement

var index = 1

interface TaskSection : ProgressReceiver {
    val puzzleContext: PuzzleContext
    val task: PuzzleTask
    val progressBar: HTMLProgressElement
    fun launch()
    fun cancel()
}

open class GenericTaskSection(
    val title: String,
    override val puzzleContext: PuzzleContext,
    override val task: PuzzleTask = { _, _ -> TODO(title) },
    val resultField: ResultField,
    val errorField: ErrorField,
    override val progressBar: HTMLProgressElement,
    val launchButton: HTMLButtonElement,
    val cancelButton: HTMLButtonElement
) : TaskSection {

    val taskLauncher = BackgroundTaskLauncher()

    override suspend fun starting() {
        console.log("Starting $title")
        launchButton.addClass("is-loading")
        cancelButton.removeClass("is-invisible")
        resultField.hide()
        errorField.hide()
        progressBar.removeClass("is-danger")
        progressBar.removeClass("is-success")
        progressBar.attributes.removeNamedItem("value")
    }

    override suspend fun success(result: String) {
        console.log(result)
        resultField.show(result)
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-invisible")
        progressBar.addClass("is-success")
        progressBar.value = progressBar.max
        js("debugger;")
    }

    override suspend fun error(message: Any?) {
        console.log(message)
        errorField.show(message.toString(), message)
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-invisible")
        progressBar.addClass("is-danger")
        progressBar.value = progressBar.max
    }

    override fun launch() {
        taskLauncher.launch(this, puzzleContext, task)
    }

    override fun cancel() {
        taskLauncher.cancel(this, puzzleContext, task)
    }


}

interface ErrorField {
    fun show(title: String, message: Any?)
    fun hide()
}

open class ErrorFigure(
    val errorItem: HTMLElement,
    val errorMessage: HTMLElement,
    val errorTag: HTMLElement
) : ErrorField {
    override fun show(title: String, message: Any?) {
        errorItem.removeClass("is-hidden")
        errorTag.textContent = JSON.stringify(message, null, 2)
        errorMessage.textContent = title
    }

    override fun hide() {
        errorItem.addClass("is-hidden")
    }
}

interface ResultField {
    fun show(result: String)
    fun hide()
}

open class ResultLevelItem(
    val resultItem: HTMLElement,
    val resultTag: HTMLElement
) : ResultField {
    override fun show(result: String) {
        resultItem.removeClass("is-invisible")
        resultTag.textContent = result
    }

    override fun hide() {
        resultItem.addClass("is-invisible")
    }

}

open class TaskSectionBuilder {

    lateinit var title: String
    lateinit var puzzleContext: PuzzleContext
    var task: PuzzleTask = { _, _ -> TODO(title) }
    protected lateinit var htmlElement: HTMLElement

    protected lateinit var progressBar: HTMLProgressElement
    protected lateinit var launchButton: HTMLButtonElement
    protected lateinit var cancelButton: HTMLButtonElement

    protected lateinit var resultField: ResultField
    protected lateinit var errorField: ErrorField

    open fun buildInBody(body: HTMLElement): TaskSection {

        body.append {
            htmlElement = section("section") {
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
                                    }
                                }
                            }
                        }
                        resultField = createResultField()
                        div("level-right") {
                            div("level-item") {
                                div("control") {
                                    cancelButton =
                                        button(classes = "delete is-large is-invisible") { }
                                }
                            }
                        }
                    }
                    progressBar = progress("progress has-background-grey-dark is-info") {
                        value = "0"
                        max = "100"
                    }

                    errorField = createErrorField()
                }
            }
        }
        val obj = constructObject()
        launchButton.onclick = { obj.launch() }
        cancelButton.onclick = { obj.cancel() }

        return obj
    }

    protected open fun constructObject(): TaskSection = GenericTaskSection(
        title,
        puzzleContext,
        task,
        resultField,
        errorField,
        progressBar,
        launchButton,
        cancelButton
    )

    protected open fun TagConsumer<HTMLElement>.createResultField(): ResultField {
        lateinit var resultItem: HTMLElement
        lateinit var resultTag: HTMLElement
        resultItem = div("level-item has-text-centered is-invisible") {
            div {
                p("heading") { +"Result: " }
                p { resultTag = code { +nbsp } }
            }
        }
        return ResultLevelItem(resultItem, resultTag)
    }

    protected open fun TagConsumer<HTMLElement>.createErrorField(): ErrorField {
        lateinit var errorItem: HTMLElement
        lateinit var errorMessage: HTMLElement
        lateinit var errorTag: HTMLElement
        errorItem = figure("box is-hidden") {
            errorMessage = p("subtitle is-5 is-failure") { }
            errorTag = pre { }
        }
        return ErrorFigure(errorItem, errorMessage, errorTag)
    }
}

fun taskSection(op: TaskSectionBuilder.() -> Unit) = TaskSectionBuilder().apply(op)
