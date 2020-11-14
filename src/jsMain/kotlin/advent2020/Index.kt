package advent2020

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.nav
import kotlinx.html.js.p
import kotlinx.html.js.section
import kotlinx.html.js.strong
import kotlinx.html.js.title
import org.w3c.dom.HTMLElement

val knownTasks = listOf(1 to "Waiting For The Start")

@JsExport
fun createIndex() {
    document.head!!.append {
        title { +"Advent of Code 2020" }
    }
    document.body!!.append {
        section("section") {
            div("container") {
                h1("title") { +"Advent of Code 2020" }
                p("subtitle") {
                    +"Well, "
                    strong { +"Kotlin-JS" }
                    +" works!"
                }
                knownTasks.forEach { (day, name) -> dayLink(day, name) }
            }
        }
    }
}

fun TagConsumer<HTMLElement>.dayLink(i: Int, title: String) {
    nav("level") {
        div("level-left") {
            div("level-item") {
                div("field has-addons") {
                    p("control") {
                        a("day${i.toString().padStart(2, '0')}", classes = "button") { +"Day $i" }
                    }
                }
            }
            div("level-item") {
                p("subtitle is-5") {
                    +title
                }
            }
        }
    }
}
