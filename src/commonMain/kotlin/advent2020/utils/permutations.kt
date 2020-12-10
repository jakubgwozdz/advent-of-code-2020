package advent2020.utils


fun permutations(n: Int): Sequence<List<Int>> {
    val a = IntArray(n) { it }
    return sequenceOf(a.toList()) + generateSequence {
        var i = a.size - 1
        while (i > 0 && a[i] <= a[i - 1]) i--
        if (i <= 0) return@generateSequence null
        var j = a.size - 1
        while (a[j] <= a[i - 1]) j--
        a.swap(i - 1, j)
        j = a.size - 1
        while (i < j) a.swap(i++, j--)
        a.toList()
    }
}

fun IntArray.swap(i: Int, j: Int) {
    val v = this[i]
    this[i] = this[j]
    this[j] = v
}

