package advent2020.day14

fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    val memory = mutableMapOf<Long, Long>()
    var andMask = "111111111111111111111111111111111111".toLong(2)
    var orMask = "000000000000000000000000000000000000".toLong(2)

    lines.forEach { line ->
        val mask = mask(line)
        if (mask != null) {
            andMask = mask.replace('X', '1').toLong(2)
            orMask = mask.replace('X', '0').toLong(2)
        } else {
            val (addr, value) = memOp(line) ?: error("Invalid line $line")
            val result = (value and andMask) or orMask
            memory[addr] = result
        }

    }

    return memory.values.sum().toString()
}

fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    val memory = mutableMapOf<Long, Long>()
    var masks = emptyList<Pair<Long, Long>>()

    lines.forEachIndexed { i, line ->
        val newMask = mask(line)
        if (newMask != null) {
            masks = sequence { floats(newMask) }.toList()
        } else {
            val (addr, value) = memOp(line) ?: error("Invalid line $line")
            masks.map { (andMask, orMask) -> (addr and andMask) or orMask }
                .forEach { addr1 -> memory[addr1] = value }
        }
    }

    return memory.values.sum().toString()
}

val maskRegex = """mask = ([01X]{36})""".toRegex()

private fun mask(line: String) = maskRegex.matchEntire(line)
    ?.destructured
    ?.component1()

val memOpRegex = """mem\[(\d+)\] = (\d+)""".toRegex()
private fun memOp(line: String) = memOpRegex.matchEntire(line)
    ?.destructured
    ?.let { (a, v) -> a.toLong() to v.toLong() }


private suspend fun SequenceScope<Pair<Long, Long>>.floats(mask: String) {
    val index = mask.indexOf('X')
    if (index < 0) {
        val andMask = mask.replace('0', '1').replace('_', '0').toLong(2)
        val orMask = mask.replace('0', '0').replace('_', '0').toLong(2)

        yield(andMask to orMask)
    } else {
        floats(mask.replaceRange(index, index + 1, "_"))
        floats(mask.replaceRange(index, index + 1, "1"))
    }
}
