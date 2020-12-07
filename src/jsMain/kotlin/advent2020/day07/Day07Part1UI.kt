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

class Day07Part1Section(genericElements: GenericTaskSectionElements, val divElem: HTMLDivElement) :
    GenericTaskSection(genericElements),
    Day07ProgressLogger {

    private val outerInnerMap = mutableMapOf<String, String>()
    private val outerDivMap = mutableMapOf<String, HTMLDivElement>()

    override suspend fun foundContaining(outerBag: String, innerBag: String, number: Int?, inOne: Int?) {

        outerInnerMap[outerBag] = innerBag
//        outerDivMap[innerBag]?.let { divElem.removeChild(it); outerDivMap.remove(innerBag) }
        divElem.append {

            outerDivMap[outerBag] = div("level") {
                div("level-item") {
                    style = """justify-content:flex-end;flex-shrink: inherit;"""
                    var bag = outerBag
                    span {
                        +nbsp
                        +"${outerInnerMap.size}: "
                        +nbsp
                    }
                    bagPresentation(bag)
                    while (bag in outerInnerMap) {
                        bag = outerInnerMap[bag]!!
                        span() {
                            +nbsp
                            +" ⇨ "
                            +nbsp
                        }
                        bagPresentation(bag)
                    }
                }
            }.also { it.scrollIntoView() }
        }
        resultField.show("${outerInnerMap.size}")
        delayIfChecked(60)
    }

    private fun TagConsumer<HTMLElement>.bagPresentation(bag: String) {
        span() {
            style = """
                padding: 0.5rem; 
                border: 1px solid white; 
                background-color: ${bag.split(' ')[1]};
                text-shadow:1px 1px 2px black;
                box-shadow: inset 0px 0px 2px 0px black;
                """.trimIndent()
            // opacity:50%
            +"$bag"
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


class Day07Part1SectionBuilder : TaskSectionBuilder() {

    lateinit var divElem: HTMLDivElement

    override fun createTaskSpecificFields(bodyBuilder: TagConsumer<HTMLElement>) = with(bodyBuilder) {
        divElem = div { }
    }


    override fun constructObject() = Day07Part1Section(genericElements(), divElem)
}

fun day07part1Section(op: Day07Part1SectionBuilder.() -> Unit) = Day07Part1SectionBuilder().apply(op)

