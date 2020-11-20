package advent2020

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.Entities.nbsp
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.article
import kotlinx.html.js.button
import kotlinx.html.js.code
import kotlinx.html.js.div
import kotlinx.html.js.i
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.p
import kotlinx.html.js.pre
import kotlinx.html.js.progress
import kotlinx.html.js.section
import kotlinx.html.js.span
import kotlinx.html.style
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLPreElement
import org.w3c.dom.HTMLProgressElement
import org.w3c.dom.Range

interface TaskSection : ProgressReceiver {
    fun launch()
    fun cancel()
}

open class GenericTaskSection(
    val title: String,
    val puzzleContext: PuzzleContext,
    val task: PuzzleTask = { _, _ -> TODO(title) },
    val resultField: ResultField,
    val errorField: ErrorField,
    val progressField: ProgressField,
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
        progressField.ongoing()
    }

    override suspend fun success(result: String) {
        console.log(result)
        resultField.show(result)
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-invisible")
        progressField.success()
    }

    override suspend fun error(message: Any?) {
        console.log(message)
        errorField.show(message.toString(), message)
        launchButton.removeClass("is-loading")
        cancelButton.addClass("is-invisible")
        progressField.error()
        js("debugger;")
    }

    override fun launch() {
        taskLauncher.launch(this, puzzleContext, task)
    }

    override fun cancel() {
        taskLauncher.cancel(this, puzzleContext, task)
    }


}

interface LogField {
    fun clear()
    fun addLines(vararg lines: String)
}

open class SimpleLogPre(val logPre: HTMLPreElement, val historySize: Int): LogField {

    private val lines = mutableListOf<String>()
    override fun clear() {
        lines.clear()
        logPre.textContent = lines.joinToString("\n")
    }

    override fun addLines(vararg lines: String) {
        this.lines += lines
        while (lines.size > historySize) this.lines.removeAt(0)
        logPre.textContent = this.lines.joinToString("\n")
        logPre.scrollTop = logPre.scrollHeight.toDouble()
    }

}

interface ProgressField {
    fun reset()
    fun ongoing()
    fun value(value: Double, max: Double)
    fun success()
    fun error()
}

open class SimpleProgressBar(val progressBar: HTMLProgressElement): ProgressField {
    override fun reset() {
        progressBar.removeClass("is-danger")
        progressBar.removeClass("is-success")
        progressBar.value = 0.0
    }

    override fun ongoing() {
        reset()
        progressBar.attributes.removeNamedItem("value")
    }

    override fun value(value: Double, max: Double) {
        progressBar.value = value
        progressBar.max = max
    }

    override fun success() {
        progressBar.addClass("is-success")
        progressBar.value = progressBar.max
    }

    override fun error() {
        progressBar.addClass("is-danger")
        progressBar.value = progressBar.max
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
    val codeElemend: HTMLElement
) : ResultField {
    override fun show(result: String) {
        resultItem.removeClass("is-invisible")
        codeElemend.textContent = result
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

    protected lateinit var launchButton: HTMLButtonElement
    protected lateinit var cancelButton: HTMLButtonElement

    protected lateinit var resultField: ResultField
    protected lateinit var errorField: ErrorField
    protected lateinit var progressField: ProgressField

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
                    progressField = createProgressBar()

                    errorField = createErrorField()

                    createTaskSpecificFields(this@append)

                }
            }
        }
        val obj = constructObject()
        launchButton.onclick = { obj.launch() }
        cancelButton.onclick = { obj.cancel() }

        return obj
    }

    protected open fun createTaskSpecificFields(div: TagConsumer<HTMLElement>) {

    }

    protected open fun constructObject(): TaskSection = GenericTaskSection(
        title,
        puzzleContext,
        task,
        resultField,
        errorField,
        progressField,
        launchButton,
        cancelButton
    )

    protected open fun TagConsumer<HTMLElement>.createResultField(): ResultField {
        lateinit var resultItem: HTMLElement
        lateinit var codeElem: HTMLElement
        lateinit var buttonElem: HTMLElement
        resultItem = div("level-item has-text-centered is-invisible") {
            div {
                p("heading") { +"Result: " }
                div("level-item has-text-centered") {
                    button(classes = "button is-small is-invisible") {
                        style = "background: 0 0; border: none;"
                        span("icon") { i("far fa-copy") }
                    }
                    p("subtitle") {
                        codeElem = code { +nbsp }
                    }
                    buttonElem = button(classes = "button is-small has-text-grey") {
                        style = "background: 0 0; border: none;"
                        span("icon") { i("far fa-copy") }
                    }
                }
            }
        }
        buttonElem.onclick = {
            val range = document.createRange()
            range.selectNodeContents(codeElem)
            js("window.getSelection().removeAllRanges(); window.getSelection().addRange(range)")
            val result = document.execCommand("copy")
            console.log(result)
        }

        return ResultLevelItem(resultItem, codeElem)
    }

    protected open fun TagConsumer<HTMLElement>.createErrorField(): ErrorField {
        lateinit var errorItem: HTMLElement
        lateinit var errorMessage: HTMLElement
        lateinit var errorTag: HTMLElement
        errorItem = article("message is-danger is-hidden") {
            div("message-header") {
                errorMessage = p { }
            }
            errorTag = pre("message-body") {
                style = "resize: vertical; white-space: pre-wrap;"
            }
        }
        return ErrorFigure(errorItem, errorMessage, errorTag)
    }

    protected open fun TagConsumer<HTMLElement>.createLogField(maxHeight: String = "30em", historySize: Int = 1000): LogField {
        val logPre = pre("box") {
            style = "max-height: $maxHeight; resize: vertical; white-space: pre-wrap;"
        }
        return SimpleLogPre(logPre, historySize)
    }

    protected open fun TagConsumer<HTMLElement>.createProgressBar(): ProgressField {
        val progressBar = progress("progress has-background-grey-dark is-info") {
            value = "0"
            max = "100"
        }
        return SimpleProgressBar(progressBar)
    }

}

fun taskSection(op: TaskSectionBuilder.() -> Unit) = TaskSectionBuilder().apply(op)
