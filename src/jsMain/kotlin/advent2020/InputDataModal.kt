package advent2020

import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.TagConsumer
import kotlinx.html.id
import kotlinx.html.js.a
import kotlinx.html.js.button
import kotlinx.html.js.div
import kotlinx.html.js.footer
import kotlinx.html.js.header
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.p
import kotlinx.html.js.section
import kotlinx.html.js.textArea
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTextAreaElement

const val inputDataModalId = "input-data-modal"
const val inputDataTextAreaId = "input-data-textarea"

class InputDataModal(private val puzzleContext: PuzzleContext) {

    fun appendTo(tagConsumer: TagConsumer<HTMLElement>) {
        tagConsumer.createInputDataModal()
    }

    private fun TagConsumer<HTMLElement>.createInputDataModal() {
        div("modal") {
            id = inputDataModalId
            div("modal-background")
            div("modal-card") {
                header("modal-card-head") {
                    p("modal-card-title") { +"Input Data" }
                    p {
                        +"(Paste from "
                        a("https://adventofcode.com/${puzzleContext.year}/day/${puzzleContext.day}/input") { +"adventofcode.com" }
                        +" if you're logged in)"
                    }

//                    button(classes = "delete") {
//                        onClickFunction = { hide() }
//                    }
                }
                section("modal-card-body") {
                    textArea(classes = "textarea is-family-monospace", rows = "15") { +puzzleContext.input; id = inputDataTextAreaId }
                }
                footer("modal-card-foot") {
                    button(classes = "button is-success") {
                        +"Save changes"
                        onClickFunction = { puzzleContext.input = inputDataTextArea.value; hide() }
                    }
                    button(classes = "button") {
                        +"Cancel"
                        onClickFunction = { inputDataTextArea.value = puzzleContext.input; hide() }
                    }
                }
            }
        }
    }

    private val inputDataModal get() = document.byId<HTMLElement>(inputDataModalId)
    private val inputDataTextArea get() = document.byId<HTMLTextAreaElement>(inputDataTextAreaId)

    fun show() = inputDataModal.addClass("is-active")
    fun hide() = inputDataModal.removeClass("is-active")


}

