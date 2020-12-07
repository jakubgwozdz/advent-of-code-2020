package advent2020

import org.w3c.xhr.XMLHttpRequest

actual fun readResource(pkg: String, name: String): String {
    return readResourceAsync("$pkg/$name")
}

fun readResourceAsync(url: String): String {
    val req = XMLHttpRequest()
    req.open("GET", url, false)
    req.send("")

    return req.responseText

}

fun readResourceInCurrentPackage() = readResourceAsync( "jakubgwozdz.txt")
