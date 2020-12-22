package advent2020

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass
import kotlin.time.TimeSource

actual fun <T> runTest(label: String, block: suspend () -> T): T = runBlocking {
    val s = TimeSource.Monotonic.markNow()
    block()
        .also { println("test $label: ${s.elapsedNow()}") }
}

actual fun <T> runTestExpect(exClass: KClass<out Throwable>, label: String, block: suspend () -> T): Unit = try {
    runTest(label, block)
    throw ExceptionNotThrownError(exClass)
} catch (e: Throwable) {
    if (!exClass.isInstance(e)) throw e else ({})()
}
