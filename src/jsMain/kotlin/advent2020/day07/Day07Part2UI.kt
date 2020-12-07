package advent2020.day07

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.TaskSectionBuilder
import kotlinx.html.Entities.nbsp
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.span
import kotlinx.html.style
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class Day07Part2Section(genericElements: GenericTaskSectionElements, val divElem: HTMLDivElement) :
    GenericTaskSection(genericElements),
    Day07ProgressLogger {

    private val outerInnerMap = mutableMapOf<String, MutableList<Triple<String, Int, Int>>>()
    private val outerDivMap = mutableMapOf<String, HTMLDivElement>()

    override suspend fun foundContaining(outerBag: String, innerBag: String, number: Int?, inOne: Int?) {
        outerInnerMap.getOrPut(outerBag) { mutableListOf() }.add(Triple(innerBag, number!!, inOne!!))
        val total = outerInnerMap[outerBag]!!.map { it.second * (1 + it.third) }.sum()

        outerDivMap[innerBag]?.let { divElem.removeChild(it); outerDivMap.remove(innerBag) }
        divElem.append {

            outerDivMap[outerBag] = div("level") {
                div("level-item") {
                    style = """justify-content:flex-start;flex-shrink: inherit;"""
                    bagPresentation(outerBag, total)
                    span {
                        +nbsp
                        +" = "
                        +nbsp
                    }
                    outerInnerMap[outerBag]!!.forEach {
                        span() {
                            +nbsp
                            +"${it.second}× "
                            +nbsp
                        }
                        bagPresentation(it.first, it.third)
                        span() {
                            +nbsp
                            +" + "
                            +nbsp
                        }
                    }
                }
            }.also { it.scrollIntoView() }
        }
        resultField.show("$total")
        delayIfChecked(160)

    }

    private fun TagConsumer<HTMLElement>.bagPresentation(bag: String, inside: Int) {
        span() {
            style = """
                padding: 0.5rem; 
                border: 1px solid white; 
                background-color: ${bag.split(' ')[1]};
                text-shadow:1px 1px 2px black;
                box-shadow: inset 0px 0px 2px 0px black;
                """.trimIndent()
            // opacity:50%
            +"$bag ($inside inside)"
        }
    }

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        while (divElem.firstChild != null) {
            divElem.removeChild(divElem.lastChild!!);
        }
        outerInnerMap.clear()
        outerDivMap.clear()
    }
}


class Day07Part2SectionBuilder : TaskSectionBuilder() {

    lateinit var divElem: HTMLDivElement

    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) = with(bodyBuilder) {
        divElem = div { }
    }


    override fun constructObject() = Day07Part2Section(genericElements(), divElem)
}

fun day07part2Section(op: Day07Part2SectionBuilder.() -> Unit) = Day07Part2SectionBuilder().apply(op)

