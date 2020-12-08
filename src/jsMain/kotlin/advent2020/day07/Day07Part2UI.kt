package advent2020.day07

import advent2020.GenericTaskSection
import advent2020.GenericTaskSectionElements
import advent2020.TaskSectionBuilder
import kotlinx.html.TagConsumer
import kotlinx.html.br
import kotlinx.html.dom.append
import kotlinx.html.js.div
import kotlinx.html.js.span
import kotlinx.html.style
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

data class BagElement(
    val mainElem: HTMLElement,
    val descElem: HTMLElement,
    val content: MutableList<BagElement>,
    val bag: String,
    val number: Int,
    var total: Int?,
)

class Day07Part2Section(genericElements: GenericTaskSectionElements, val divElem: HTMLDivElement) :
    GenericTaskSection(genericElements),
    Day07ProgressLogger {

    private val currentStack = mutableListOf<BagElement>()
    private val bagsFirstElems = mutableMapOf<String, BagElement>()

    override suspend fun entering(outerBag: String?, innerBag: String, number: Int) {

        console.log("==> '$outerBag' contains $number '$innerBag'")

        if (outerBag == null) {
            check(currentStack.isEmpty()) { "'$currentStack' not empty" }
            val bagElement = createTopLevelBag(innerBag).also { it.mainElem.scrollIntoView() }
            currentStack.add(bagElement)
        } else {
            val outerElem = currentStack.last()
            val innerElem = createInnerBag(outerElem, innerBag, number).also { it.mainElem.scrollIntoView() }
            currentStack.add(innerElem)
            outerElem.content.add(innerElem)
        }
        delayIfChecked(160)

    }

    override suspend fun alreadyKnown(outerBag: String) {
        console.log("already know '$outerBag'")

        // copy here
        bagsFirstElems[outerBag]!!.content.forEach {
            currentStack.last().mainElem.appendChild(it.mainElem.cloneNode(true))
            currentStack.last().mainElem.lastElementChild!!.scrollIntoView()
        }

        delayIfChecked(160)
    }


    override suspend fun exiting(outerBag: String?, innerBag: String, number: Int, insideEach: Int) {
        console.log("<== '$outerBag' contains $number '$innerBag', $insideEach in each")

        currentStack.removeLast().also {
            if (insideEach != 0) {
                it.descElem.append {
                    br { }
                    span {
                        +"($insideEach ${if (number != 1) "inside each" else "inside"})"
                    }
                }
            }
            bagsFirstElems.getOrPut(it.bag) { it }
        }

        delayIfChecked(160)
    }

    private fun createTopLevelBag(bag: String): BagElement {
        lateinit var result: BagElement
        divElem.append {
            div("level") {
                div("level-item") {
                    style = """justify-content:flex-start;flex-shrink: inherit;"""
                    result = bagPresentation(bag, 1)
                }
            }
        }
        return result
    }

    private fun createInnerBag(outerElem: BagElement, bag: String, number: Int): BagElement {
        lateinit var result: BagElement
        outerElem.mainElem.append {
            result = bagPresentation(bag, number)
        }
        return result
    }

    private fun TagConsumer<HTMLElement>.bagPresentation(
        bag: String,
        number: Int,
    ): BagElement {
        lateinit var descElem: HTMLElement
        val mainElem = div("bag") {
            style = """background-color: ${bag.split(' ')[1]};"""
            descElem = div("bag-desc") {
                if (number != 1) +"$number × "
                +bag
            }
        }
        return BagElement(mainElem, descElem, mutableListOf(), bag, number, null)
    }

    override suspend fun starting() {
        super<GenericTaskSection>.starting()
        while (divElem.firstChild != null) {
            divElem.removeChild(divElem.lastChild!!);
        }
        currentStack.clear()
        bagsFirstElems.clear()
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

