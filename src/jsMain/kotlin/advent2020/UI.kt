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

fun createUI(day: Int?) {
    if (day==null) {
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
                    dayLink(1, "Chronal Calibration")
                }
            }
        }
    } else {
        document.head!!.append {
            title { +"Day $day of Advent of Code 2020" }
        }
    }
}

fun TagConsumer<HTMLElement>.dayLink(i: Int, title: String) {
    nav("level is-mobile") {
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
