package advent2020

import kotlinx.browser.document
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.h1
import kotlinx.html.js.p
import kotlinx.html.js.section
import kotlinx.html.js.title


fun createHeader(day: Int) {
    val title = knownTasks.firstOrNull { it.first == day }?.second ?: ""

    document.head!!.append {
        title { +"$day: $title" }
    }
    document.body!!.append {
        section("section") {
            div("container") {
                h1("title") { +title }
                p("subtitle") { +"Day $day of Advent of Code 2020" }
            }
        }
    }
}

