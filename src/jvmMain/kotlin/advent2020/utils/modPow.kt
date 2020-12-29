package advent2020.utils

import java.math.BigInteger

actual fun modPow(a: Long, b: Long, m: Long): Long {
    return BigInteger.valueOf(a).modPow(BigInteger.valueOf(b), BigInteger.valueOf(m)).longValueExact()
}
