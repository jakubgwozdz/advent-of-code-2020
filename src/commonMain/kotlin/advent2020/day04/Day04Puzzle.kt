package advent2020.day04

import advent2020.day04.PassportField.byr
import advent2020.day04.PassportField.cid
import advent2020.day04.PassportField.ecl
import advent2020.day04.PassportField.eyr
import advent2020.day04.PassportField.hcl
import advent2020.day04.PassportField.hgt
import advent2020.day04.PassportField.iyr
import advent2020.day04.PassportField.pid


fun part1(input: String): String {
    val lines = input.trim().lineSequence()

    val passports = passports(lines)

    val result = passports.count(Passport::hasRequiredFields)

    return result.toString()
}

fun part2(input: String): String {
    val lines = input.trim().lineSequence()

    val passports = passports(lines)

    val result = passports.count { passport ->
        passport.hasRequiredFields() && passport.fields().all(PassportFieldValue::isValid)
    }
    return result.toString()
}


enum class PassportField { byr, iyr, eyr, hgt, hcl, ecl, pid, cid }

typealias PassportFieldValue = Pair<PassportField, String>
typealias PassportLine = List<PassportFieldValue>
typealias Passport = List<PassportLine>

fun Passport.fields() = flatten()
fun Passport.hasRequiredFields() = fields().map { it.first }.toSet().run { size == 8 || size == 7 && cid !in this }

fun PassportFieldValue.isValid(): Boolean {
    val (k, v) = this
    return when (k) {
        byr -> v.toIntOrNull() in (1920..2002)
        iyr -> v.toIntOrNull() in (2010..2020)
        eyr -> v.toIntOrNull() in (2020..2030)
        hgt -> when {
            v.endsWith("cm") -> v.dropLast(2).toIntOrNull() in (150..193)
            v.endsWith("in") -> v.dropLast(2).toIntOrNull() in (59..76)
            else -> false
        }
        hcl -> v.matches("""#[0-9a-f]{6}""".toRegex())
        ecl -> v.matches("(amb|blu|brn|gry|grn|hzl|oth)".toRegex())
        pid -> v.matches("""\d{9}""".toRegex())
        cid -> true
    }
}

private fun passports(lines: Sequence<String>): Sequence<Passport> = sequence {

    var currentPassport = mutableListOf<PassportLine>()

    lines.map { line ->
        line.split(' ').filterNot { it.isBlank() }
            .map { kv -> kv.split(':').let { PassportField.valueOf(it[0]) to it[1] } }
    }
        .forEach {
            if (it.isEmpty()) {
                yield(currentPassport.toList())
                currentPassport = mutableListOf()
            } else {
                currentPassport.add(it)
            }
        }
    if (currentPassport.isNotEmpty()) {
        yield(currentPassport.toList())
        currentPassport = mutableListOf()
    }
}

