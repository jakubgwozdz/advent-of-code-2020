package advent2020.day25

fun part1(input: String): String {
    val lines = input.trim().lines()
    val cardPK = lines[0].toLong()
    val doorPK = lines[1].toLong()
    val enc = calcEnc(cardPK, doorPK)
    return enc.toString()

}

fun calcEnc(cardPK: Long, doorPK: Long): Long {
    val m = 20201227L
    var acc = 1L
    var loopSize = 0L
    while (true) {
        loopSize++
        acc = (acc * 7) % m
        if (acc == cardPK) return modPow(doorPK, loopSize, m)
        if (acc == doorPK) return modPow(cardPK, loopSize, m)
    }
}

// using property (A * B) % C = (A % C * B % C) % C
fun modPow(base: Long, exp: Long, m: Long): Long =
    exp.toString(2).map { if (it == '1') 1 else 0 }
        .reversed()
        .fold(ModPowState(1L, base % m)) { acc, base2digit ->
            val (prev, pow2mod) = acc
            val next = if (base2digit == 1) prev * pow2mod % m else prev
            ModPowState(next, (pow2mod * pow2mod % m))
        }
        .result

data class ModPowState(val result: Long, var pow2mod: Long)
