package advent2020

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.awaitAnimationFrame
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

val fancyShadow = "box-shadow: 0 0.5em 1em -0.125em rgba(0,0,0,.2), 0 0 0 1px rgba(10,10,10,.02);"

class AnimationTimer {
    var time = window.performance.now()
    var lastOverkill = 0.0

    suspend fun await(): Double {
        val newTime = window.awaitAnimationFrame()
        val dt = newTime - time
        time = newTime
        return dt.coerceAtMost(200.0) // at most 200ms
    }

    fun reset(): Double {
        time = window.performance.now()
        return time
    }

    suspend fun delay(i: Int): Boolean {
        val startedAt = time
        val updatedWaitTime = i - lastOverkill
        val expectedEndTime = window.performance.now() + updatedWaitTime
        var dt = 0.0
        while (dt < updatedWaitTime) {
            dt += await()
        }
        if (i == 0 && window.performance.now() > time + 50) await()
        else if (i > 0) lastOverkill = window.performance.now() - expectedEndTime

        return time > startedAt

    }
}
