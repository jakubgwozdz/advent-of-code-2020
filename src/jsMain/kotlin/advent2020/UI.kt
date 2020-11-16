package advent2020

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.*
import org.w3c.dom.Document
import org.w3c.dom.HTMLElement


fun createHeader(day: Int, puzzleContext: PuzzleContext) {
    val title = knownTasks.firstOrNull { it.first == day }?.second ?: ""

    val inputDataModal = InputDataModal(puzzleContext)

    document.head!!.append {
        title { +"$day: $title" }
    }
    document.body!!.append {
        createHeader(day, title, inputDataModal)
        inputDataModal.appendTo(this)
    }
}

private fun TagConsumer<HTMLElement>.createHeader(day: Int, title: String, inputDataModal: InputDataModal) {
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
            }
            div("navbar-menu") {
                div("navbar-end") {
                    div("navbar-item") {
                        a("..", classes = "icon is-medium") {
                            i("fas fa-2x fa-home")
                        }
                    }
                    div("navbar-item") {
                        a("https://github.com/jakubgwozdz/advent-of-code-2020", classes = "icon is-medium") {
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
