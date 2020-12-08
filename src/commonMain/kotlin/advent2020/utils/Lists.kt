package advent2020.utils

fun <T> Sequence<T>.groups(separatorOp: (T) -> Boolean): Sequence<List<T>> = sequence {

    var currentGroup = mutableListOf<T>()

    forEach {
        if (separatorOp(it)) {
            yield(currentGroup.toList())
            currentGroup = mutableListOf()
        } else {
            currentGroup.add(it)
        }
    }

    if (currentGroup.isNotEmpty()) {
        yield(currentGroup.toList())
    }
}

fun String.groupSequence() = this.trim().lineSequence().groups { it.isEmpty() }


fun <E : Comparable<E>> MutableList<E>.binaryInsert(e: E, onExisting: (Int, E) -> Unit = this::add) {
    val pos = binarySearch(e)
    if (pos >= 0) onExisting(pos, e)
    else add(-pos - 1, e)
}
