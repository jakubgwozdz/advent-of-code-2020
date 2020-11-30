package advent2020

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

actual fun <T> runTest(block: suspend () -> T): T = runBlocking { block() }

actual fun <T> runTestExpect(exClass: KClass<out Throwable>, block: suspend () -> T): Unit = try {
    runTest(block)
    throw ExceptionNotThrownError(exClass)
} catch (e: Throwable) {
    if (!exClass.isInstance(e)) throw e else ({})()
}
