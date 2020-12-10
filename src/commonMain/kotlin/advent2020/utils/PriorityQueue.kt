package advent2020.utils

open class Queue<E : Any> {

    protected var queue: ArrayList<E> = ArrayList(11)

    val size get() = queue.size

    fun isNotEmpty(): Boolean = size > 0

    fun poll():E {
        check(size > 0)
        return queue.removeAt(0)
    }

    open fun offer(e: E) {
        queue.add(e)
    }

}


class PriorityQueue<E : Any>(val comparator: Comparator<E>): Queue<E>() {

    override fun offer(e: E) {
        val index = queue.binarySearch(e, comparator).let {
            if (it < 0) -it -1 else it
        }
        queue.add(index, e)
    }

}
