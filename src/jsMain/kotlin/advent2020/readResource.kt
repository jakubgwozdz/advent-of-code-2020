package advent2020

import org.w3c.xhr.XMLHttpRequest

actual fun readResource(pkg: String, name: String): String {

    val req = XMLHttpRequest()
    val url = "/advent2020/$pkg/$name"
    req.open("GET", url, false)
    req.send("")

    return req.responseText

}
