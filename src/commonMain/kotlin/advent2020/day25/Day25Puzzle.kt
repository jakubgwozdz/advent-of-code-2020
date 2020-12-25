package advent2020.day25

fun part1(input: String): String {
    val lines = input.trim().lines()
    val cardPK = lines[0].toLong()
    val doorPK = lines[1].toLong()
    val enc = calcEnc(cardPK, doorPK)
    return enc.toString()

}

fun calcEnc(cardPK: Long, doorPK: Long): Long {
    val m = 20201227
    var acc = 1L
    var loopSize = 0L
    while (true) {
        loopSize++
        acc = (acc * 7) % m
        if (acc == cardPK) return modPow(doorPK, loopSize, m)
        if (acc == doorPK) return modPow(cardPK, loopSize, m)
    }
}

fun modPow(base: Long, exp: Long, m: Int): Long {
    // using property (A * B) % C = (A % C * B % C) % C

    // which powers of 2 are used in exponent
    val binary = exp.toString(2).reversed().mapIndexedNotNull { index, c -> if (c == '1') index else null }

    // precalculate for powers of 2
    val modPowsOf2 = mutableMapOf<Int, Long>()
    modPowsOf2[0] = base % m
    (1..binary.maxOrNull()!!).forEach { modPowsOf2[it] = (modPowsOf2[it - 1]!! * modPowsOf2[it - 1]!!) % m }

    return binary.map { modPowsOf2[it]!! }
        .fold(1L) { acc, l -> (acc % m) * (l % m) } % m
}
