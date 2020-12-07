package advent2020

import java.io.InputStream
import java.io.InputStreamReader

actual fun readResource(pkg: String, name: String): String =
    (object {}.javaClass.getResource("$pkg/$name").content as InputStream).use {
        InputStreamReader(it).readText()
    }
        .also { println("read $pkg $name") }
