package advent2020

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.*
import org.w3c.dom.HTMLElement

val knownTasks = listOf(1 to "Waiting For The Start")

@JsExport
fun createIndex() {
    document.head!!.append {
        title { +"Advent of Code 2020" }
    }
    document.body!!.append {
        nav("navbar") {
            div("container") {
                div("navbar-brand") {
                    div("navbar-item") {
                        div {
                            h1("title") { +"Advent of Code 2020" }
                            h2("subtitle") { +"Well, press F12 to see how "; strong { +"Kotlin-JS" }; +" works!" }
                        }
                    }
                }
                div("navbar-menu") {
                    div("navbar-end") {
                        div("navbar-item") {
                            a("https://github.com/jakubgwozdz/advent-of-code-2020", classes = "icon is-medium") {
                                i("fab fa-2x fa-github")
                            }
                        }
                    }
                }
            }
        }

        section("section") {
            div("container") {
                knownTasks.forEach { (day, name) -> dayLink(day, name) }
            }
        }
    }
}

fun TagConsumer<HTMLElement>.dayLink(i: Int, title: String) {
    p("subtitle is-5") {
        a("day${i.toString().padStart(2, '0')}") { +"Day $i: $title" }
    }
}
