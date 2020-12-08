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
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class Day07Part1Section(genericElements: GenericTaskSectionElements, val divElem: HTMLDivElement) :
        GenericTaskSection(genericElements),
        Day07ProgressLogger {

    private val outerInnerMap = mutableMapOf<String, String>()
    private val outerDivMap = mutableMapOf<String, HTMLDivElement>()

    override suspend fun foundContaining(outerBag: String, innerBag: String) {

        outerInnerMap[outerBag] = innerBag
//        outerDivMap[innerBag]?.let { divElem.removeChild(it); outerDivMap.remove(innerBag) }
        divElem.append {

            outerDivMap[outerBag] = div("bag-with-id") {
                span {
                    +"${outerInnerMap.size}: "
                    +nbsp
                }
                bagPresentation(outerBag, outerInnerMap)
            }.also { it.scrollIntoView() }
        }
        resultField.show("${outerInnerMap.size}")
        delayIfChecked(60)
    }

    private fun TagConsumer<HTMLElement>.bagPresentation(
            bag: String,
            outerInnerMap: MutableMap<String, String>
    ) {
        div("bag") {
            style = """background-color: ${bag.split(' ')[1]};"""
            div("bag-desc") { +bag }
            outerInnerMap[bag]?.let { bagPresentation(it, outerInnerMap) }
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
        div() {
            style = "display: flex;"
            divElem = div() {
                style = """justify-content:flex-end;flex-shrink: inherit;"""
            }
        }
        Unit
    }


    override fun constructObject() = Day07Part1Section(genericElements(), divElem)
}

fun day07part1Section(op: Day07Part1SectionBuilder.() -> Unit) =
        Day07Part1SectionBuilder().apply(op)

