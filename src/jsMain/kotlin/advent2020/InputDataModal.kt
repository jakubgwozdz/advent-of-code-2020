package advent2020

import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.classes
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.button
import kotlinx.html.js.div
import kotlinx.html.js.footer
import kotlinx.html.js.header
import kotlinx.html.js.p
import kotlinx.html.js.section
import kotlinx.html.js.textArea
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTextAreaElement

class InputDataModal(private val htmlElement: HTMLElement) {
    fun show() = htmlElement.addClass("is-active")
    fun hide() = htmlElement.removeClass("is-active")
}

fun createInputDataModal(puzzleInfo: PuzzleInfo, puzzleContext: PuzzleContext, readOnly: Boolean = false): InputDataModal {
    lateinit var inputDataModal: HTMLElement
    lateinit var inputDataTextArea: HTMLTextAreaElement
    lateinit var saveButton: HTMLButtonElement
    lateinit var cancelButton: HTMLButtonElement

    document.body!!.append {
        inputDataModal = div("modal") {
            div("modal-background")
            div("modal-card") {
                header("modal-card-head") {
                    p("modal-card-title") { +"Input Data" }
                    div("content") {
                        p {
                            if (readOnly) classes += "is-hidden"
                            +"(Use the content from "
                            a("https://adventofcode.com/${puzzleInfo.year}/day/${puzzleInfo.day}/input") { +"adventofcode.com" }
                            +" if you're logged in)"
                        }
                    }
                }
                section("modal-card-body") {
                    inputDataTextArea =
                        textArea(classes = "textarea is-family-monospace", rows = "15") { +puzzleContext.input }
                }
                footer("modal-card-foot") {
                    saveButton = button(classes = "button is-success") {
                        if (readOnly) classes += "is-hidden"
                        +"Save changes"
                    }
                    cancelButton = button(classes = "button") {
                        +"Cancel"
                    }
                }
            }
        }
    }

    val result = InputDataModal(inputDataModal)

    // actions must be set after the object is created
    saveButton.onclick = { puzzleContext.input = inputDataTextArea.value; result.hide() }
    cancelButton.onclick = { inputDataTextArea.value = puzzleContext.input; result.hide() }

    return result

}
