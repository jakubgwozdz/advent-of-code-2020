package advent2020.day04

import advent2020.ProgressLogger
import advent2020.day04.PassportField.byr
import advent2020.day04.PassportField.cid
import advent2020.day04.PassportField.ecl
import advent2020.day04.PassportField.eyr
import advent2020.day04.PassportField.hcl
import advent2020.day04.PassportField.hgt
import advent2020.day04.PassportField.iyr
import advent2020.day04.PassportField.pid
import advent2020.utils.groupSequence

interface Day04ProgressLogger : ProgressLogger {
    suspend fun validPassport(passport: Passport) {}
    suspend fun invalidPassportMissingFields(passport: Passport, missingFields: Set<PassportField>) {}
    suspend fun invalidPassportInvalidFields(passport: Passport, invalidFields: Set<PassportFieldValue>) {}
}

val dummyReceiver = object : Day04ProgressLogger {}

suspend fun part1(input: String, logger: ProgressLogger = dummyReceiver): String {
    logger as Day04ProgressLogger

    val passports = input.groupSequence().map(::Passport)

    val result = passports.count { passport ->
        val missingFields = passport.missingFields()
        if (missingFields.isEmpty()) logger.validPassport(passport) else logger.invalidPassportMissingFields(passport, missingFields)
        missingFields.isEmpty()
    }

    return result.toString()
}

suspend fun part2(input: String, logger: ProgressLogger = dummyReceiver): String {
    logger as Day04ProgressLogger

    val passports = input.groupSequence().map(::Passport)

    val result = passports.count { passport ->
        val missingFields = passport.missingFields()
        when(missingFields.isEmpty()) {
            true -> {
                val invalidFields = passport.invalidFields()

                when (invalidFields.isEmpty()) {
                    true -> {
                        logger.validPassport(passport)
                        true
                    }
                    false -> {
                        logger.invalidPassportInvalidFields(passport, invalidFields)
                        false
                    }
                }
            }
            false -> {
                logger.invalidPassportMissingFields(passport, missingFields)
                false
            }
        }
    }
    return result.toString()
}


enum class PassportField { byr, iyr, eyr, hgt, hcl, ecl, pid, cid }

data class PassportFieldValue(val field: PassportField, val value: String) {
    constructor(kv: String) : this(PassportField.valueOf(kv.substringBefore(":")), kv.substringAfter(":"))
}

data class Passport(val lines: List<String>, val fields: List<PassportFieldValue>) {
    constructor(lines: List<String>) : this(lines, lines.map { parseLine(it)}.flatten())
}

fun Passport.missingFields() = PassportField.values().toSet() - fields.map { it.field } - cid
fun Passport.invalidFields() = fields.filter { !it.isValid() }.toSet()

fun PassportFieldValue.isValid(): Boolean = when (field) {
    byr -> value.toIntOrNull() in (1920..2002)
    iyr -> value.toIntOrNull() in (2010..2020)
    eyr -> value.toIntOrNull() in (2020..2030)
    hgt -> when {
        value.endsWith("cm") -> value.dropLast(2).toIntOrNull() in (150..193)
        value.endsWith("in") -> value.dropLast(2).toIntOrNull() in (59..76)
        else -> false
    }
    hcl -> value.matches("""#[0-9a-f]{6}""".toRegex())
    ecl -> value.matches("(amb|blu|brn|gry|grn|hzl|oth)".toRegex())
    pid -> value.matches("""\d{9}""".toRegex())
    cid -> true
}

private fun parseLine(line: String) = line.split(' ')
    .filterNot { it.isBlank() }
    .map(::PassportFieldValue)

