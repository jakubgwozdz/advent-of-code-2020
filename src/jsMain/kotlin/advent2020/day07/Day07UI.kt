package advent2020.day07

import advent2020.PuzzleContext
import advent2020.PuzzleInfo
import advent2020.createHeader
import advent2020.readResourceInCurrentPackage
import kotlinx.browser.document
import kotlinx.html.dom.append
import kotlinx.html.style
import kotlinx.html.unsafe

val day07puzzleContext by lazy { PuzzleContext(readResourceInCurrentPackage()) }
val day07puzzleInfo = PuzzleInfo("day07", "Handy Haversacks (Visual)", 7, 2020)

@JsExport
fun createUI() {

    createHeader(day07puzzleInfo, day07puzzleContext, readOnly = true)

    //language=CSS
    val style = """.bag { 
    padding: 0.5rem;
    border: 1px solid rgba(10,10,10,.1);
    border-radius: 5px;
    text-shadow: 1px 1px 2px #282f2f;
    box-shadow: 4px 4px 14px 4px #282f2f;
    float: right;
    margin-left: 5px;
    margin-bottom: 5px;
}

.bag-desc {
    float: left;
    margin-left: 5px;
    margin-bottom: 5px;
}



"""

    document.body!!.append {
        style { unsafe { raw(style) } }
    }

    day07part1Section {
        title = "Part 1"
        subtitle = "Different bags that CAN eventually contain a 'shiny gold' one"
        puzzleContext = day07puzzleContext
        task = ::part1
        delay = true
    }.buildInBody(document.body!!)

    day07part2Section {
        title = "Part 2"
        subtitle = "Total bags inside a 'shiny gold' one"
        puzzleContext = day07puzzleContext
        task = ::part2
        delay = true
    }.buildInBody(document.body!!)

}
