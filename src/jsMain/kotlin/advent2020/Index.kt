package advent2020

import kotlinx.browser.document
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.h2
import kotlinx.html.js.i
import kotlinx.html.js.nav
import kotlinx.html.js.p
import kotlinx.html.js.section
import kotlinx.html.js.strong
import kotlinx.html.js.title
import org.w3c.dom.HTMLElement

data class PuzzleInfo(val path: String, val title: String, val day: Int, val year: Int = 2020)

val knownTasks by lazy {
    listOf(
        advent2020.day01.day01puzzleInfo,
        advent2020.day02.day02puzzleInfo,
        advent2020.day03.day03puzzleInfo,
        advent2020.day04.day04puzzleInfo,
        advent2020.day05.day05puzzleInfo,
        advent2020.day06.day06puzzleInfo,
    )
}

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
                knownTasks.forEach { dayLink(it) }
            }
        }
    }
}

fun TagConsumer<HTMLElement>.dayLink(info: PuzzleInfo) {
    p("subtitle is-5") {
        a(info.path) { +"Day ${info.day}: ${info.title}" }
    }
}
