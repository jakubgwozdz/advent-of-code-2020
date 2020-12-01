package advent2020

import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.Entities.nbsp
import kotlinx.html.InputType.checkBox
import kotlinx.html.LABEL
import kotlinx.html.TagConsumer
import kotlinx.html.classes
import kotlinx.html.dom.append
import kotlinx.html.js.article
import kotlinx.html.js.button
import kotlinx.html.js.code
import kotlinx.html.js.div
import kotlinx.html.js.i
import kotlinx.html.js.input
import kotlinx.html.js.label
import kotlinx.html.js.p
import kotlinx.html.js.pre
import kotlinx.html.js.progress
import kotlinx.html.js.section
import kotlinx.html.js.span
import kotlinx.html.style
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLProgressElement

interface TaskSection : ProgressReceiver {
    fun launch()
    fun cancel()
}

class GenericTaskSectionElements(
    val title: String,
    val puzzleContext: PuzzleContext,
    val task: PuzzleTask,
    val resultField: ResultField,
    val errorField: ReportField,
    val progressField: ProgressField,
    val launchButton: HTMLButtonElement,
    val delayCheckbox: CheckboxField,
    val cancelButton: HTMLButtonElement
)

open class GenericTaskSection(
    val title: String,
    val puzzleContext: PuzzleContext,
    val task: PuzzleTask = { _, _ -> TODO(title) },
    val resultField: ResultField,
    val errorField: ReportField,
    val progressField: ProgressField,
    val launchButton: HTMLButtonElement,
    val delayCheckbox: CheckboxField,
    val cancelButton: HTMLButtonElement
) : TaskSection {

    constructor(genericElements: GenericTaskSectionElements) : this(
        genericElements.title,
        genericElements.puzzleContext,
        genericElements.task,
        genericElements.resultField,
        genericElements.errorField,
        genericElements.progressField,
        genericElements.launchButton,
        genericElements.delayCheckbox,
        genericElements.cancelButton
    )

    val taskLauncher = BackgroundTaskLauncher()

    var runWithDelay: Boolean = false

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
        runWithDelay = delayCheckbox.state
        taskLauncher.launch(this, puzzleContext, task)
    }

    override fun cancel() {
        taskLauncher.cancel(this, puzzleContext, task)
    }


}

interface ReportField {
    fun clear()
    fun hide()
    fun show(message: Any?)
    fun show(title: String, message: Any?)
    fun addLines(vararg lines: String)
}

open class SimpleReportFigure(
    val wholeElement: HTMLElement,
    val titleElement: HTMLElement,
    val preElement: HTMLElement,
    val historySize: Int
) : ReportField {
    private val lines = mutableListOf<String>()
    override fun clear() {
        lines.clear()
        preElement.textContent = lines.joinToString("\n")
    }

    override fun addLines(vararg lines: String) {
        this.lines += lines.flatMap { it.lines() }
        while (lines.size > historySize) this.lines.removeAt(0)
        preElement.textContent = this.lines.joinToString("\n")
        preElement.scrollTop = preElement.scrollHeight.toDouble()
        wholeElement.removeClass("is-hidden")
    }

    override fun show(title: String, message: Any?) {
        titleElement.textContent = title
        show(message)
    }

    override fun show(message: Any?) {
        this.lines.clear()
        this.lines += (if (message is String) message else JSON.stringify(message, null, 2)).lines()
        preElement.textContent = this.lines.joinToString("\n")
        preElement.scrollTop = preElement.scrollHeight.toDouble()
        wholeElement.removeClass("is-hidden")
    }

    override fun hide() {
        wholeElement.addClass("is-hidden")
    }


}

interface ProgressField {
    fun reset()
    fun ongoing()
    fun value(value: Double, max: Double)
    fun value(value: Int, max: Int) = value(value.toDouble(), max.toDouble())
    fun success()
    fun error()
}

open class SimpleProgressBar(val progressBar: HTMLProgressElement) : ProgressField {
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

interface CheckboxField {
    var state: Boolean
}

open class CheckboxWithLabel(val item: HTMLElement, val checkbox: HTMLInputElement) : CheckboxField {
    override var state: Boolean
        get() = checkbox.checked
        set(value) {
            checkbox.checked = value
        }
}

interface ResultField {
    fun show(result: String)
    fun hide()
}

open class ResultLevelItem(
    val resultItem: HTMLElement,
    val codeElement: HTMLElement
) : ResultField {
    override fun show(result: String) {
        resultItem.removeClass("is-invisible")
        codeElement.textContent = result
    }

    override fun hide() {
        resultItem.addClass("is-invisible")
    }

}

open class TaskSectionBuilder {

    lateinit var title: String
    lateinit var puzzleContext: PuzzleContext
    var task: PuzzleTask = { _, _ -> TODO(title) }
    var delay: Boolean? = null
    protected lateinit var htmlElement: HTMLElement

    protected lateinit var launchButton: HTMLButtonElement
    protected lateinit var cancelButton: HTMLButtonElement

    protected lateinit var resultField: ResultField
    protected lateinit var errorField: ReportField
    protected lateinit var progressField: ProgressField

    protected lateinit var delayCheckbox: CheckboxField

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
                            div("level-item") {
                                div("control") {
                                    delayCheckbox = createCheckboxField(delay, "Delay")
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

    // helper function for deriving builders
    protected fun genericElements() = GenericTaskSectionElements(
        title,
        puzzleContext,
        task,
        resultField,
        errorField,
        progressField,
        launchButton,
        delayCheckbox,
        cancelButton
    )

    protected open fun constructObject(): TaskSection = GenericTaskSection(
        title,
        puzzleContext,
        task,
        resultField,
        errorField,
        progressField,
        launchButton,
        delayCheckbox,
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

    protected open fun TagConsumer<HTMLElement>.createCheckboxField(checked: Boolean?, label: String): CheckboxField {
        return createCheckboxField(checked) { +" $label" }
    }

    protected open fun TagConsumer<HTMLElement>.createCheckboxField(
        checked: Boolean?,
        block: LABEL.() -> Unit = {}
    ): CheckboxField {
        lateinit var resultItem: HTMLElement
        lateinit var checkbox: HTMLInputElement
        resultItem = label("checkbox") {
            if (checked == null) classes += "is-hidden"
            checkbox = input(checkBox) {
                this.checked = checked == true // disable for null or false
            }
            block()
        }
        return CheckboxWithLabel(resultItem, checkbox)
    }

    protected open fun TagConsumer<HTMLElement>.createReportField(
        title: String = "",
        historySize: Int = 1000,
        maxHeight: String? = null,
        isDanger: Boolean = false
    ): ReportField {
        lateinit var wholeElement: HTMLElement
        lateinit var titleElement: HTMLElement
        lateinit var preElement: HTMLElement
        wholeElement = article("message is-hidden ${if (isDanger) "is-danger " else ""}") {
            div("message-header") {
                titleElement = p { +title }
            }
            preElement = pre("message-body") {
                val extraStyle = if (maxHeight != null) "max-height: $maxHeight;" else ""
                style = "resize: vertical; white-space: pre-wrap; $extraStyle"
            }
        }
        return SimpleReportFigure(wholeElement, titleElement, preElement, 1000)
    }

    protected open fun TagConsumer<HTMLElement>.createErrorField(): ReportField {
        return createReportField(isDanger = true)
    }

    protected open fun TagConsumer<HTMLElement>.createLogField(
        title: String = "Log",
        maxHeight: String = "30em",
        historySize: Int = 1000
    ): ReportField {
        return createReportField(title, historySize, maxHeight)
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
