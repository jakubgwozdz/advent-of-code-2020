package advent2020

import kotlinx.browser.document
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.p
import kotlinx.html.js.section
import kotlinx.html.js.strong

fun createUI() {
    document.body!!.append {
        section("section") {
            div("container") {
                h1("title") { +"Hello There" }
                p("subtitle") {
                    +"Well, "
                    strong { +"Kotlin" }
                    +" works!"
                }
                a(classes = "button is-primary") { +"Button" }
            }
        }
    }
}
