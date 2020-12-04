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
            }
        }
    }

    return InputSection(htmlElement)
}

open class InputSection(val htmlElement: HTMLElement)


