import advent2020.createUI
import kotlinx.browser.document
import kotlinx.browser.window

val funfunfun = ::createUI

fun main() {
    val day = Regex("(.*/)?day(\\d+)(/.*)?")
        .matchEntire(window.location.pathname)
        ?.destructured?.component2()?.toInt()
        .also { console.log(it) }
    console.log(funfunfun)
    document.addEventListener("DOMContentLoaded", { createUI(day) })
}
