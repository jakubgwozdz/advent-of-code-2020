package advent2020

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.button
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.p
import kotlinx.html.js.section
import org.w3c.dom.HTMLElement

fun createInputSectionWithModal(puzzleInfo: PuzzleInfo, puzzleContext: PuzzleContext, readOnly: Boolean = false) {
    val inputDataModal = createInputDataModal(puzzleContext, readOnly)
    createVisibleInputSection(puzzleInfo, inputDataModal, readOnly)
}

fun createVisibleInputSection(
    puzzleInfo: PuzzleInfo,
    inputDataModal: InputDataModal,
    readOnly: Boolean = false
): InputSection {
    lateinit var htmlElement: HTMLElement
    document.body!!.append {
        htmlElement = section("section") {
            div("container") {
                div("level") {
                    div("level-left") {
                        div("level-item") {
                            p("title is-3") { +"Input Data" }
                        }
                        div("level-item") {
                            div("control") {
                                button(classes = "button") {
                                    +if (readOnly) "View" else "Edit"
                                    onClickFunction = { inputDataModal.show() }
                                }
                            }
                        }
                    }
                }
                div("content") {
                    if (readOnly) classes += "is-hidden"
                    p {
                        +"(Use the content from "
                        a("https://adventofcode.com/${puzzleInfo.year}/day/${puzzleInfo.day}/input") { +"adventofcode.com" }
                        +" if you're logged in)"
                    }
                }
            }
        }
    }

    return InputSection(htmlElement)
}

open class InputSection(val htmlElement: HTMLElement)


