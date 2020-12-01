import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


// PARAMS

val day = 1
val year = 2020
val pkg = "day01"


// INIT
val root = projectRoot()
val cookie = readCookie(root)

// FETCH

val input = fetchInput(cookie, day, year)

// CREATE FILES

val className = pkg.capitalize()
val context = mapOf(
    "\${pkg}" to pkg,
    "\${className}" to className,
    "\${input}" to input,
    "\${day}" to day.toString(),
    "\${year}" to year.toString(),
)

// input data in file
fileFromString(
    root.resolve(Paths.get("src", "commonMain", "resources", "advent2020", pkg, "jakubgwozdz")),
    input
)

// input data as kotlin variable
fileFromTemplate(
    root.resolve(Paths.get("src", "commonMain", "kotlin", "advent2020", pkg, "JakubGwozdz.kt")),
    Paths.get("commonMain.input.kt.template"),
    context
)

// file consistency test template
fileFromTemplate(
    root.resolve(Paths.get("src", "jvmTest", "kotlin", "advent2020", pkg, "${className}InputConsistencyTest.kt")),
    Paths.get("jvmTest.inputTest.kt.template"),
    context
)

// task kotlin file template
fileFromTemplate(
    root.resolve(Paths.get("src", "commonMain", "kotlin", "advent2020", pkg, "${className}Puzzle.kt")),
    Paths.get("commonMain.kt.template"),
    context
)

// test template
fileFromTemplate(
    root.resolve(Paths.get("src", "commonTest", "kotlin", "advent2020", pkg, "${className}PuzzleTest.kt")),
    Paths.get("commonTest.puzzleTest.kt.template"),
    context
)

// UI class template
fileFromTemplate(
    root.resolve(Paths.get("src", "jsMain", "kotlin", "advent2020", pkg, "${className}UI.kt")),
    Paths.get("jsMain.ui.kt.template"),
    context
)

// index.html template
fileFromTemplate(
    root.resolve(Paths.get("src", "jsMain", "resources", pkg, "index.html")),
    Paths.get("jsMain.index.html.template"),
    context
)


// FUNCTIONS

fun projectRoot(): Path {
    val pwd = Paths.get(".").toAbsolutePath().normalize()
    var parent: Path? = pwd
        .also { println("working dir is $it") }
    while (parent != null && parent.fileName.toString() != "advent-of-code-2020") {
        parent = parent.parent
    }
    return parent ?: pwd
}

/**
 * Expect file `local/cookie` in project dir, with content like `session=53616c74.....`
 */
fun readCookie(root: Path): String {
    val path = root.resolve(Paths.get("local", "cookie")).also { println("reading cookie from $it ...") }
    return Files.readString(path).trim().also { println("cookie read, ${it.length} chars long")}
}

fun fetchInput(cookie: String, day: Int, year: Int = 2020): String {
    val url = "https://adventofcode.com/$year/day/$day/input"

    println("fetching input from $url ...")

    val resp = HttpClient.newBuilder().build().send(
        HttpRequest.newBuilder().GET()
            .header("Cookie", cookie)
            .uri(URI.create(url))
            .build(),
        HttpResponse.BodyHandlers.ofString()
    )

    check(resp.statusCode() in (200..299))
    return resp.body().also { println("input fetched, ${it.length} chars long") }

}

fun fileFromTemplate(outputPath: Path, inputPath: Path, context: Map<String, String>) {
    println("reading template from $inputPath ...")
    val templateRegex = Regex("\\$\\{.*?\\}")
    val template = Files.readString(inputPath)
    val content = template.replace(templateRegex) { m ->
        context[m.value] ?: error("unknown template variable ${m.value}")
    }
    fileFromString(outputPath, content)
}

fun fileFromString(outputPath: Path, content: String) {
    println("writing generated file to $outputPath ...")
    Files.createDirectories(outputPath.parent)
    Files.writeString(outputPath, content)
    println("generated file written")
}
