import kotlinx.browser.window

/**
 * poor man's setup as I don't know how to export that better
 */
fun main() {
    val global = window.asDynamic()
    val mod = js("module")
    global["advent2020"] = mod.exports.advent2020
}
