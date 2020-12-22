package advent2020

import kotlin.reflect.KClass

expect fun <T> runTest(label: String = "", block: suspend () -> T): T

expect fun <T> runTestExpect(exClass: KClass<out Throwable>, label: String = "", block: suspend () -> T): Unit

class ExceptionNotThrownError(val exClass: KClass<out Throwable>) : AssertionError("${exClass.simpleName} not thrown")
