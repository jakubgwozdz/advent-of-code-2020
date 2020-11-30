import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


// PARAMS

val day = 25
val year = 2019
val pkg = "day25x"


// INIT
val className = pkg.capitalize()
val root = projectRoot()
val cookie = readCookie(root)

// FETCH

val input = fetchInput(cookie, day, year)

// CREATE FILES

val tq = "\"\"\"" // helper for templates - triple quote

// input data in file

with(root.resolve(Paths.get("src", "commonMain", "resources", "advent2020", pkg, "jakubgwozdz"))) {
    Files.createDirectories(this.parent)
    Files.writeString(this, input)
}

// input data as kotlin variable

with(root.resolve(Paths.get("src", "commonMain", "kotlin", "advent2020", pkg, "JakubGwozdz.kt"))) {
    Files.createDirectories(this.parent)
    val content ="""package advent2020.$pkg

val ${pkg}myPuzzleInput get() = $tq
$input$tq.trim()
    """.trimIndent()
    Files.writeString(this, content)
}

// task kotlin file template

with(root.resolve(Paths.get("src", "commonMain", "kotlin", "advent2020", pkg, "${className}Puzzle.kt"))) {
    Files.createDirectories(this.parent)
    val content ="""package advent2020.$pkg

import advent2020.ProgressReceiver
import advent2020.linesAsFlowOfLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed

interface ${className}Part1ProgressReporter : ProgressReceiver {
}

interface ${className}Part2ProgressReporter : ProgressReceiver {
}

suspend fun part1(input: String, receiver: ProgressReceiver): String {
    val lines = input.lines()

    TODO()
}

suspend fun part2(input: String, receiver: ProgressReceiver): String {
    val lines = input.lines()

    TODO()
}

""".trimIndent()
    Files.writeString(this, content)
}

//






























// FUNCTIONS

fun projectRoot(): Path {
    val pwd = Paths.get(".").toAbsolutePath().normalize()
    var parent: Path? = pwd
//        .also { println("working dir is $it") }
    while (parent != null && parent.fileName.toString() != "advent-of-code-2020") {
        parent = parent.parent
    }
    return parent ?: pwd
}

/**
 * Expect file `local/cookie` in project dir, with content like `session=53616c74.....`
 */
fun readCookie(root: Path): String {
    return Files.readString(root.resolve(Paths.get("local", "cookie"))).trim()
}

fun fetchInput(cookie: String, day: Int, year: Int = 2020): String {
    val url = "https://adventofcode.com/$year/day/$day/input"

    val resp = HttpClient.newBuilder().build().send(
        HttpRequest.newBuilder().GET()
            .header("Cookie", cookie)
            .uri(URI.create(url))
            .build(),
        HttpResponse.BodyHandlers.ofString()
    )

    check(resp.statusCode() in (200..299))
    return resp.body()

}
