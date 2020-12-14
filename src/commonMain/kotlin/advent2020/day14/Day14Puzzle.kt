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

    lines.forEach { line ->
        val newMask = mask(line)
        if (newMask != null) {
            masks = sequence {
                val queue = mutableListOf(newMask)
                while (queue.isNotEmpty()) {
                    val mask = queue.removeFirst()
                    val index = mask.indexOf('X')
                    if (index >= 0) {
                        queue.add(mask.replaceRange(index, index + 1, "_"))
                        queue.add(mask.replaceRange(index, index + 1, "1"))
                    } else {
                        val andMask = mask.replace('0', '1').replace('_', '0').toLong(2)
                        val orMask = mask.replace('0', '0').replace('_', '0').toLong(2)
                        yield(andMask to orMask)
                    }
                }
            }.toList()
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


