package advent2020

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.reflect.KClass

actual fun <T> runTest(block: suspend () -> T): dynamic = GlobalScope.promise { block() }

actual fun <T> runTestExpect(exClass: KClass<out Throwable>, block: suspend () -> T): dynamic =
    GlobalScope.promise { block() }
        .then(
            { throw ExceptionNotThrownError(exClass) },
            { e -> if (!exClass.isInstance(e)) throw e else ({})() }
        )

