package advent2020

import kotlinx.coroutines.runBlocking

actual fun <T> runTest(block: suspend () -> T): T = runBlocking { block() }
