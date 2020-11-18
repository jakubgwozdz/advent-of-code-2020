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
import kotlinx.html.js.title
import org.w3c.dom.HTMLElement


fun createHeader(puzzleInfo: PuzzleInfo) {
    document.head!!.append { title { +"${puzzleInfo.day}: ${puzzleInfo.title}" } }
    document.body!!.append { createHeader(puzzleInfo) }
}

private fun TagConsumer<HTMLElement>.createHeader(puzzleInfo: PuzzleInfo) {
    nav("navbar") {
        div("container") {
            div("navbar-brand") {
                div("navbar-item") {
                    div {
                        h1("title") { +puzzleInfo.title }
                        h2("subtitle") { +"Day ${puzzleInfo.day} of Advent of Code ${puzzleInfo.year}" }
                    }
                }
            }
            div("navbar-menu") {
                div("navbar-end") {
                    div("navbar-item") {
                        a("..", classes = "icon is-medium") {
                            i("fas fa-2x fa-home")
                        }
                    }
                    div("navbar-item") {
                        a(
                            "https://github.com/jakubgwozdz/advent-of-code-2020",
                            classes = "icon is-medium"
                        ) {
                            i("fab fa-2x fa-github")
                        }
                    }
                }
            }
        }
    }
}

