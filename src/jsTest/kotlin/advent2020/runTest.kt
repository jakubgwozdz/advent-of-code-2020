package advent2020

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.reflect.KClass
import kotlin.time.TimeSource

actual fun <T> runTest(label: String, block: suspend () -> T): dynamic = GlobalScope.promise {
    val s = TimeSource.Monotonic.markNow()
    println("test $label: ")
    block()
        .also { println("${s.elapsedNow()}; ") }
}

actual fun <T> runTestExpect(exClass: KClass<out Throwable>, label: String, block: suspend () -> T): dynamic =
    GlobalScope.promise { block() }
        .then(
            { throw ExceptionNotThrownError(exClass) },
            { e -> if (!exClass.isInstance(e)) throw e else ({})() }
        )

