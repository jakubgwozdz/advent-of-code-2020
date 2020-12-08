package advent2020.day07

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.TaskSectionBuilder
import kotlinx.html.TagConsumer
import kotlinx.html.br
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.style
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class Day07Part2Section(genericElements: GenericTaskSectionElements, val divElem: HTMLDivElement) :
    GenericTaskSection(genericElements),
    Day07ProgressLogger {

    private val outerInnerMap = mutableMapOf<String, MutableList<Triple<String, Int, Int?>>>()
    private val bagDivMap = mutableMapOf<String, HTMLDivElement>()

    override suspend fun foundContaining(
        outerBag: String,
        innerBag: String,
        number: Int?,
        inOne: Int?
    ) {
        console.log("$outerBag contains $number $innerBag, in each: $inOne" )
        if (inOne == null) return // TODO: I should probably do it better and prepare divs here
        outerInnerMap.getOrPut(outerBag) { mutableListOf() }
            .add(Triple(innerBag, number!!, inOne))
        val total = outerInnerMap[outerBag]!!.map { it.second * (1 + (it.third?:0)) }.sum()

        bagDivMap[outerBag]?.let { divElem.removeChild(it); bagDivMap.remove(outerBag) }
        outerInnerMap[outerBag]?.forEach { (innerBag, _, _) ->
            bagDivMap[innerBag]?.let { divElem.removeChild(it); bagDivMap.remove(innerBag) }
        }

        divElem.append {

            bagDivMap[outerBag] = div("level") {
                div("level-item") {
                    style = """justify-content:flex-start;flex-shrink: inherit;"""
                    bagPresentation(outerBag, 1, outerInnerMap)
                }
            }.also { it.scrollIntoView() }
        }
        resultField.show("$total")
        delayIfChecked(160)

    }

    private fun TagConsumer<HTMLElement>.bagPresentation(
        bag: String,
        number: Int,
        outerInnerMap: Map<String, List<Triple<String, Int, Int?>>>
    ) {
        val total = outerInnerMap[bag]?.map { it.second * (1 + (it.third?:0)) }?.sum()
        div("bag") {
            style = """background-color: ${bag.split(' ')[1]};"""
            div("bag-desc") {
                if (number != 1) +"$number × "
                +bag
                if (total != null) {
                    br { }
                    +"($total ${if (number != 1) "inside each" else "inside"})"
                }
            }
            outerInnerMap[bag]?.forEach {
                bagPresentation(it.first, it.second, outerInnerMap)
            }
        }
    }

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        while (divElem.firstChild != null) {
            divElem.removeChild(divElem.lastChild!!);
        }
        outerInnerMap.clear()
        bagDivMap.clear()
    }
}


class Day07Part2SectionBuilder : TaskSectionBuilder() {

    lateinit var divElem: HTMLDivElement

    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) =
        with(bodyBuilder) {
            divElem = div { }
        }


    override fun constructObject() = Day07Part2Section(genericElements(), divElem)
}

fun day07part2Section(op: Day07Part2SectionBuilder.() -> Unit) =
    Day07Part2SectionBuilder().apply(op)

