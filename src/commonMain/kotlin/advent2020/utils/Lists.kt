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


//inline fun <T> Iterable<T>.atLeast(count: Int, predicate: (T) -> Boolean) = count(predicate) >= count
inline fun <T> Iterable<T>.atLeast(count: Int, predicate: (T) -> Boolean): Boolean {
    if (count <= 0) return true
    if (this is Collection && isEmpty()) return false
    var c = 0
    for (element in this) if (predicate(element)) {
        c++
        if (c >= count) return true
    }
    return false
}

