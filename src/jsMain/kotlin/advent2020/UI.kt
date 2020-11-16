package advent2020

import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.js.*
import org.w3c.dom.Document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTextAreaElement


fun createHeader(day: Int, puzzleContext: PuzzleContext) {
    val title = knownTasks.firstOrNull { it.first == day }?.second ?: ""

    document.head!!.append {
        title { +"$day: $title" }
    }
    document.body!!.append {
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
                                onClickFunction = { showInputDataModal() }
                            }
                        }
                    }
                }
                div("navbar-menu") {
                    div("navbar-end") {
                        div("navbar-item") {
                            a("..", classes = "icon is-medium") {
                                i("fas fa-2x fa-backward")
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
        div("modal") {
            id = inputDataModalId
            div("modal-background")
            div("modal-card") {
                header("modal-card-head") {
                    p("modal-card-title") { +"Input Data" }
                    button(classes = "delete") {
                        onClickFunction = { hideInputDataModal() }
                    }
                }
                section("modal-card-body") {
                    textArea(classes = "textarea") { +puzzleContext.input ; id = inputDataTextAreaId }
                }
                footer("modal-card-foot") {
                    button(classes = "button is-success") {
                        +"Save changes"
                        onClickFunction = { puzzleContext.input = inputDataTextArea.value ; hideInputDataModal() }
                    }
                    button(classes = "button") {
                        +"Cancel"
                        onClickFunction = { inputDataTextArea.value = puzzleContext.input ; hideInputDataModal() }
                    }
                }
            }
        }
    }
}

const val inputDataModalId = "input-data-modal"
val inputDataModal = document.byId<HTMLElement>(inputDataModalId)

fun showInputDataModal() = inputDataModal.addClass("is-active")
fun hideInputDataModal() = inputDataModal.removeClass("is-active")

const val inputDataTextAreaId = "input-data-textarea"
val inputDataTextArea get() = document.byId<HTMLTextAreaElement>(inputDataTextAreaId)

inline fun <reified K : HTMLElement> Document.byId(elementId: String) =
    getElementById(elementId) as K
