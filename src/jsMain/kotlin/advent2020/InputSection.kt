package advent2020

import kotlinx.browser.document
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.button
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.p
import kotlinx.html.js.section
import org.w3c.dom.HTMLElement

fun createInputSectionWithModal(puzzleInfo: PuzzleInfo, puzzleContext: PuzzleContext) {
    val inputDataModal = createInputDataModal(puzzleContext)
    createVisibleInputSection(puzzleInfo, inputDataModal)
}

fun createVisibleInputSection(puzzleInfo: PuzzleInfo, inputDataModal: InputDataModal) : InputSection {
    lateinit var htmlElement: HTMLElement
    document.body!!.append {
        htmlElement = section("section") {
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

    return InputSection(htmlElement)
}

open class InputSection(val htmlElement: HTMLElement)


