package advent2020.utils

fun Sequence<String>.groups(): Sequence<List<String>> = sequence {

    var currentGroup = mutableListOf<String>()

    forEach {
        if (it.isEmpty()) {
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

fun String.groupSequence() = this.trim().lineSequence().groups()

