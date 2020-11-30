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

// file consistency test template
fileFromTemplate(
    root.resolve(Paths.get("src", "jvmTest", "kotlin", "advent2020", pkg, "${className}InputConsistencyTest.kt")),
    Paths.get("jvmTest.inputTest.kt.template"),
    context
)

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

fun fileFromTemplate(outputPath: Path, inputPath: Path, context: Map<String, String>) {
    val templateRegex = Regex("\\$\\{.*?\\}")
    val template = Files.readString(inputPath)
    val content = template.replace(templateRegex) { m ->
        context[m.value] ?: error("unknown template variable ${m.value}")
    }
    fileFromString(outputPath, content)
}

fun fileFromString(outputPath: Path, content: String) {
    Files.createDirectories(outputPath.parent)
    Files.writeString(outputPath, content)
}
