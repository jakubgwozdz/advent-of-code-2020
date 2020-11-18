package advent2020

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.button
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.h2
import kotlinx.html.js.i
import kotlinx.html.js.nav
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.p
import kotlinx.html.js.section
import kotlinx.html.js.title
import org.w3c.dom.Document
import org.w3c.dom.HTMLElement


fun createHeader(puzzleInfo: PuzzleInfo, puzzleContext: PuzzleContext) {

    val inputDataModal = InputDataModal(puzzleContext)

    document.head!!.append {
        title { +"${puzzleInfo.day}: ${puzzleInfo.title}" }
    }
    document.body!!.append {
        createHeader(puzzleInfo)
        inputDataModal.appendTo(this)
        createInputSection(puzzleInfo, inputDataModal)
    }
}

private fun TagConsumer<HTMLElement>.createHeader(
    puzzleInfo: PuzzleInfo
) {
    nav("navbar") {
        div("container") {
            div("navbar-brand") {
                div("navbar-item") {
                    div {
                        h1("title") { +puzzleInfo.title }
                        h2("subtitle") { +"Day ${puzzleInfo.day} of Advent of Code ${puzzleInfo.year}" }
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

private fun TagConsumer<HTMLElement>.createInputSection(
    puzzleInfo: PuzzleInfo,
    inputDataModal: InputDataModal
) {
    section("section") {
        div("container") {
            div("level") {
                div("level-left") {
                    div("level-item") {
                        h1("title") { +"Input Data" }
                    }
                    div("level-item") {
                        div("control") {
                            button(classes = "button") {
                                +"Edit"
                                onClickFunction = { inputDataModal.show() }
                            }
                        }
                    }
                }
            }
            div("content") {
                p {
                    +"(Use the content from "
                    a("https://adventofcode.com/${puzzleInfo.year}/day/${puzzleInfo.day}/input") { +"adventofcode.com" }
                    +" if you're logged in)"
                }
            }
        }
    }
}

inline fun <reified K : HTMLElement> Document.byId(elementId: String) =
    getElementById(elementId) as K

