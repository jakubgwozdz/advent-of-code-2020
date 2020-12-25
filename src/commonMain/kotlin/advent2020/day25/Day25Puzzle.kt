package advent2020.day25

fun part1(input: String): String {
    val lines = input.trim().lines()
    val cardPK = lines[0].toLong()
        .also { println(" cardPK $it; ") }
    val doorPK = lines[1].toLong()
        .also { println(" doorPK $it; ") }

    val cardLoop =
        (
//                if (cardPK == 13233401L) 16679169L else
                calcLoop(cardPK))
            .also { println(" cardLoop $it; ") }

    val doorLoop =
        (
//                if (doorPK == 6552760L) 7725933L else
                calcLoop(doorPK))
            .also { println(" doorLoop $it; ") }

    val enc1 = transform(cardPK, doorLoop)
        .also { println(" enc1 $it; ") }
    val enc2 = transform(doorPK, cardLoop)
        .also { println(" enc2 $it; ") }

    return enc1.toString()
}

private fun calcLoop(devicePK: Long): Long {
//    val timer = Monotonic.markNow()
//    var nextPrint = 10.seconds
    var result = 0L
    while (true) {
        val transform = transform(7, result)
        if (transform == devicePK)
            return result
        else result++

//        val elapsed = timer.elapsedNow()
//        if (elapsed > nextPrint) {
//            println("$elapsed: $result moves ; ")
//            nextPrint += 10.seconds
//        }
    }
}

val modPowsOf2for7 by lazy { calcModPowsOf2(7) }

private fun calcModPowsOf2(a: Long): Map<Int, Long> {
    val r = mutableMapOf<Int, Long>()
    r[0] = a % 20201227
    (1..63).forEach {
        r[it] = (r[it - 1]!! * r[it - 1]!!) % 20201227
    }
    return r.toMap()
}

//private fun dummy(s: Any?) {}
private fun transform(a: Long, b: Long): Long {

//    return modPow(a, b, 20201227)
//    val report = if (a!=7L) ::println else ::dummy

//    if (a==16679169L) { println(a) }
//    report(" a = $a ")

    val modPowsOf2 = if (a == 7L) modPowsOf2for7 else calcModPowsOf2(a)

    val powers = b.toString(2).reversed().mapIndexedNotNull { index, c -> if (c == '1') index else null }
//        .also {report(" powers = $it ")}

    return powers.map { modPowsOf2[it]!! }
//        .also {report(" modPowsOf2 = $it ")}
        .fold(1L) { acc, l -> (acc % 20201227) * (l % 20201227) } % 20201227
}

fun part2(input: String): String {
    val lines = input.trim().lines()

    TODO()
}

