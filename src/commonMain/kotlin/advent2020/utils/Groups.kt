package advent2020.utils

fun groups(lines: Sequence<String>): Sequence<List<String>> = sequence {

    var currentGroup = mutableListOf<String>()

    lines
        .forEach {
            if (it.isEmpty()) {
                yield(currentGroup.toList())
                currentGroup = mutableListOf()
            } else {
                currentGroup.add(it)
            }
        }

    if (currentGroup.isNotEmpty()) {
        yield(currentGroup.toList())
        currentGroup = mutableListOf()
    }
}

fun String.groupSequence() = groups(this.trim().lineSequence())
