package advent2020.day10

// Disclaimer, my solution was much uglier (although it worked in decent time complexity)
// This was developed after the hint from a friend
// Also, I should've looked at the puzzle name, which literally was "Adapter Array"
// It is now true O(n)

fun part1(input: String): String {

    val adapters = input.trim().lines().map(String::toInt)
    val array = BooleanArray(adapters.maxOrNull()!! + 1)
    adapters.forEach { array[it] = true }

    var sinceLast = 0
    var ones = 0
    var threes = 0

    array.forEach { exists ->
        if (exists) {
            if (sinceLast == 1) ones++
            if (sinceLast == 3) threes++
            sinceLast = 0
        }
        sinceLast++
    }

    threes++ // at the end there is 3 to the device

    return (ones * threes).toString()
}

fun part2(input: String): String {

    val adapters = input.trim().lines().map(String::toInt)

    val array = BooleanArray(adapters.maxOrNull()!! + 1)
    adapters.forEach { array[it] = true }
    val variations = LongArray(array.size)
    variations[0] = 1L
    array.forEachIndexed { index, exists ->
        if (exists) {
            var v = 0L
            if (index > 0) v += variations[index - 1]
            if (index > 1) v += variations[index - 2]
            if (index > 2) v += variations[index - 3]
            variations[index] = v
        }
    }
    val last = variations.last()

    return last.toString()

}

