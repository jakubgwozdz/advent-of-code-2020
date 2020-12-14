package advent2020.day14

fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    val memory = mutableMapOf<Long, Long>()
    var andMask = "111111111111111111111111111111111111".toLong(2)
    var orMask =  "000000000000000000000000000000000000".toLong(2)

    lines.forEach { line ->
        val newMask = """mask = ([01X]{36})""".toRegex().matchEntire(line)?.destructured?.component1()
        if (newMask != null) {
            andMask = newMask.replace('X','1').toLong(2)
            orMask = newMask.replace('X','0').toLong(2)
        }
        else {
            val memOp =
                """mem\[(\d+)\] = (\d+)""".toRegex().matchEntire(line)?.destructured ?: error("Invalid line $line")
            val (addr, value) = memOp.let { (a, v) -> a.toLong() to v.toLong() }
            val result = (value and andMask) or orMask
            memory[addr] = result
        }

    }

    return memory.values.sum().toString()
}

fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    val memory = mutableMapOf<Long, Long>()
    var mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

    lines.forEachIndexed { i, line ->
        println("${(i+1).toString().padStart(4)}: $line")
        val newMask = """mask = ([01X]{36})""".toRegex().matchEntire(line)?.destructured?.component1()
        if (newMask != null) mask = newMask
        else {
            val memOp =
                """mem\[(\d+)\] = (\d+)""".toRegex().matchEntire(line)?.destructured ?: error("Invalid line $line")
            val (addr, value) = memOp.let { (a, v) -> a.toLong() to v.toLong() }

            floats(addr, mask)
//                .onEach{ addr1 -> println("memory[$addr1] = $value"}
                .forEach{ addr1 -> memory[addr1] = value}

        }

    }

    return memory.values.sum().toString()
}

fun floats(addr:Long, mask:String) : Sequence<Long> = sequence {
    val index = mask.indexOf('X')
    if (index < 0) {
        val bits = addr.toString(2).padStart(mask.length, '0')
        val masked = bits.mapIndexed { index, c ->
            when (mask[index]) {
                '_' -> '0'
                '0' -> c
                '1' -> '1'
                else -> error("invalid mask $mask")
            }
        }.toCharArray().concatToString()
//        println("mask $mask changed address $addr ($bits) to ${masked.toLong(2)} ($masked)")
        yield(masked.toLong(2))
    } else {
        val newMask0 = mask.replaceRange(index, index+1,"_")
        val newMask1 = mask.replaceRange(index, index+1,"1")

        yieldAll(floats(addr, newMask0))
        yieldAll(floats(addr, newMask1))
    }


}
