package advent2020

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.*
import org.w3c.dom.Document
import org.w3c.dom.HTMLElement


fun createHeader(day: Int, puzzleContext: PuzzleContext, progressReporter: ProgressReporter) {
    val title = knownTasks.firstOrNull { it.first == day }?.second ?: ""

    val inputDataModal = InputDataModal(puzzleContext)
    val taskLauncher = TaskLauncher(puzzleContext, progressReporter)

    document.head!!.append {
        title { +"$day: $title" }
    }
    document.body!!.append {
        createHeader(day, title, inputDataModal, taskLauncher)
        inputDataModal.appendTo(this)
    }
}

private fun TagConsumer<HTMLElement>.createHeader(
    day: Int,
    title: String,
    inputDataModal: InputDataModal,
    taskLauncher: TaskLauncher
) {
    nav("navbar") {
        div("container") {
            div("navbar-brand") {
                div("navbar-item") {
                    div {
                        h1("title") { +title }
                        h2("subtitle") { +"Day $day of Advent of Code 2020" }
                    }
                }
                div("navbar-item") {
                    div("control") {
                        button(classes = "button") {
                            +"Input Data"
                            onClickFunction = { inputDataModal.show() }
                        }
                    }
                }
                div("navbar-item") {
                    div("control") {
                        button(classes = "button") {
                            +"Run Part 1"
                            onClickFunction = { taskLauncher.launchPart1() }
                        }
                    }
                }
                div("navbar-item") {
                    div("control") {
                        button(classes = "button") {
                            +"Run Part 2"
                            onClickFunction = { taskLauncher.launchPart2() }
                        }
                    }
                }
            }
            div("navbar-menu") {
                div("navbar-end") {
                    div("navbar-item") {
                        a("..", classes = "icon is-medium") {
                            i("fas fa-2x fa-home")
                        }
                    }
                    div("navbar-item") {
                        a(
                            "https://github.com/jakubgwozdz/advent-of-code-2020",
                            classes = "icon is-medium"
                        ) {
                            i("fab fa-2x fa-github")
                        }
                    }
                }
            }
        }
    }
}

inline fun <reified K : HTMLElement> Document.byId(elementId: String) =
    getElementById(elementId) as K

class TaskLauncher(val puzzleContext: PuzzleContext, val progressReporter: ProgressReporter) {

    var activeJob: Job? = null

    fun launchPart1() {
        activeJob?.let { if (it.isActive) it.cancel() }
        activeJob = GlobalScope.launch {
            puzzleContext.launchPart1(progressReporter)
        }
    }

    fun launchPart2() {
        activeJob?.let { if (it.isActive) it.cancel() }
        activeJob = GlobalScope.launch {
            puzzleContext.launchPart2(progressReporter)
        }
    }


}
