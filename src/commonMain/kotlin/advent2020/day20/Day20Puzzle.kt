package advent2020.day20

import advent2020.ProgressLogger
import advent2020.day20.Edge.*
import advent2020.utils.groups

enum class Edge { Top, Right, Bottom, Left }
data class Orientation(val topEdge: Edge, val flip: Boolean)

fun List<Char>.edgeCode() = joinToString("") { if (it == '#') "1" else "0" }.toLong(2)

data class Tile(val id: Long, val lines: List<String>) {
    val size = lines.count()
    private val top by lazy { lines.first().toList().edgeCode() }
    private val topR by lazy { lines.first().toList().reversed().edgeCode() }

    private val right by lazy { lines.map { it.last() }.edgeCode() }
    private val rightR by lazy { lines.map { it.last() }.reversed().edgeCode() }

    private val bottomR by lazy { lines.last().toList().edgeCode() }
    private val bottom by lazy { lines.last().toList().reversed().edgeCode() }

    private val leftR by lazy { lines.map { it.first() }.edgeCode() }
    private val left by lazy { lines.map { it.first() }.reversed().edgeCode() }

    private val edgesCodes by lazy { listOf(top, right, bottom, left, topR, rightR, bottomR, leftR) }

    fun at(y: Int, x: Int, o: Orientation = Orientation(Top, false)): Char {
        val (y1, x1) = orient(y, x, o)
        return when {
            x1 !in lines.indices -> ' '
            y1 !in lines.indices -> ' '
            else -> lines[y1][x1]
        }
    }

    fun orient(y: Int, x: Int, o: Orientation) = when (o.topEdge) {
        Top -> if (!o.flip) y to x else y to size - 1 - x
        Right -> if (!o.flip) x to size - 1 - y else x to y
        Bottom -> if (!o.flip) size - 1 - y to size - 1 - x else size - 1 - y to x
        Left -> if (!o.flip) size - 1 - x to y else size - 1 - x to size - 1 - y
    }

    fun match(o: Tile) = when {
        top in o.edgesCodes -> Top
        right in o.edgesCodes -> Right
        bottom in o.edgesCodes -> Bottom
        left in o.edgesCodes -> Left
        else -> null
    }
}

interface Day20ProgressLogger : ProgressLogger {
    suspend fun foundTile(tile: Tile) {}
    suspend fun foundMatch(id1: Long, edge: Edge, id2: Long) {}
    suspend fun tilePlaced(id: Long, row: Int, col: Int) {}
    suspend fun tileOriented(id: Long, orientation: Orientation) {}
    suspend fun allTilesFound() {}
    suspend fun allMatchesFound() {}
    suspend fun allTilesPlaced() {}
    suspend fun imageComposed(image: Tile) {}
    suspend fun monsterFound(y: Int, x: Int, o: Orientation, pixels: List<Pair<Int, Int>>) {}
}

suspend fun part1(input: String, logger: ProgressLogger = object : Day20ProgressLogger {}): String {
    logger as Day20ProgressLogger

    val tiles = tiles(input, logger)

    val matches = matches(tiles, logger)

    val result = matches.filterValues { it.size == 2 }
        .keys.fold(1L) { a, i -> a * i }

    return result.toString()
}

private suspend fun matches(tiles: Map<Long, Tile>, logger: Day20ProgressLogger) =
    tiles.map { (id1, t1) ->
        id1 to tiles
            .filter { (id2, _) -> id1 != id2 }
            .map { (id2, t2) -> t1.match(t2)?.also { logger.foundMatch(id1, it, id2) }?.let { it to id2 } }
            .filterNotNull()
            .toMap()
    }
        .toMap()
        .also { logger.allMatchesFound() }

private suspend fun tiles(input: String, logger: Day20ProgressLogger) = input.trim().lineSequence()
    .groups { it.isBlank() }
    .toList()
    .map { tileLines ->
        val id = """Tile (\d+):""".toRegex().matchEntire(tileLines[0])?.destructured?.component1()?.toLong()
            ?: error("not id @ ${tileLines[0]}")
        Tile(id, tileLines.drop(1))
            .also { logger.foundTile(it) }
    }.associateBy { it.id }
    .also { logger.allTilesFound() }

suspend fun part2(input: String, logger: ProgressLogger = object : Day20ProgressLogger {}): String {
    logger as Day20ProgressLogger

    val tiles = tiles(input, logger)

    val matches = matches(tiles, logger)

    val width = matches.count { (_, e) -> e.size == 3 } / 4 + 2
    val (jigsaw, orientations) = placeTiles(matches, logger)

    logger.allTilesPlaced()

    val tileSize = tiles.values.first().size
    val image = Array((tileSize - 2) * width) { Array((tileSize - 2) * width) { ' ' } }

    // copy to large image
    jigsaw.indices.forEach { r ->
        jigsaw[r].indices.forEach { c ->
            val tile = jigsaw[r][c]
            val orientation = orientations[r][c]

            (1 until tileSize - 1).forEach { y ->
                (1 until tileSize - 1).forEach { x ->
                    val at = tiles[tile]!!.at(y, x, orientation)
                    image[r * (tileSize - 2) + y - 1][c * (tileSize - 2) + x - 1] = at
                }
            }
        }
    }

    val full = Tile(0, image.map { line -> line.joinToString("") })

    logger.imageComposed(full)

    val monster = monster.lines()
        .flatMapIndexed { y: Int, l: String -> l.mapIndexedNotNull { x, c -> if (c == '#') y to x else null } }

    val monsters = Edge.values().flatMap { listOf(Orientation(it, false), Orientation(it, true)) }
        .sumBy { o ->
            (0..image.size).sumBy { y ->
                (0..image.size).count { x ->
                    monster.all { (my, mx) -> full.at(y + my, x + mx, o) == '#' }
                        .also {
                            if (it) logger.monsterFound(
                                y, x, o,
                                monster.map { (my, mx) -> full.orient(y + my, x + mx, o) })
                        }
                }
            }
        }

    val hashes = image.sumBy { it.count { c -> c == '#' } }

    val result = hashes - monster.size * monsters

    return result.toString()
}

val monster by lazy {
    """
        |                  # 
        |#    ##    ##    ###
        | #  #  #  #  #  #   
        """.trimMargin()
}

private suspend fun placeTiles(
    matches: Map<Long, Map<Edge, Long>>,
    logger: Day20ProgressLogger
): Pair<Array<Array<Long>>, Array<Array<Orientation>>> {
    val byNeighbours = matches.entries.groupBy { (_, m) -> m.size }
        .mapValues { (_, v) -> v.map { it.key } }
    val cornerTiles = byNeighbours[2]!!
    val edgeTiles = byNeighbours[3]!!
    val insideTiles = byNeighbours[4]!!

    val width = edgeTiles.size / 4 + 2
    val jigsaw: Array<Array<Long>> = Array(width) { Array(width) { 0L } }
    val orientations: Array<Array<Orientation>> = Array(width) { Array(width) { Orientation(Top, false) } }
    val used = mutableSetOf<Long>()

    // first corner
    cornerTiles.first().let { next ->
        place(next, 0, 0, jigsaw, used, logger)
    }
    // first of first edge
    run {
        val r = 0
        val c = 1
        val prev = jigsaw[r][c - 1]
        val next = matches[prev]!!.values.first { it in edgeTiles && it !in used }
        place(next, r, c, jigsaw, used, logger)
    }
    // first of second edge
    run {
        val r = 1
        val c = 0
        val prev = jigsaw[r - 1][c]
        val next = matches[prev]!!.values.first { it in edgeTiles && it !in used }
        place(next, r, c, jigsaw, used, logger)
    }
    // orient fist corner with adjacent
    run {
        orient(orientations, jigsaw, 0, 0, matches[jigsaw[0][0]]!!, logger)
        orient(orientations, jigsaw, 1, 0, matches[jigsaw[1][0]]!!, logger)
        orient(orientations, jigsaw, 0, 1, matches[jigsaw[0][1]]!!, logger)
    }


    // rest of first edge
    (2 until width - 1).forEach { c ->
        val r = 0
        val prev = jigsaw[r][c - 1]
        val next = matches[prev]!!.values.first { it in edgeTiles && it !in used }
        place(next, r, c, jigsaw, used, logger)
        orient(orientations, jigsaw, r, c, matches[next]!!, logger)
    }
    // rest of second edge
    (2 until width - 1).forEach { r ->
        val c = 0
        val prev = jigsaw[r - 1][c]
        val next = matches[prev]!!.values.first { it in edgeTiles && it !in used }
        place(next, r, c, jigsaw, used, logger)
        orient(orientations, jigsaw, r, c, matches[next]!!, logger)
    }
    // second corner
    cornerTiles.first { jigsaw[0][width - 2] in matches[it]!!.values && it !in used }.let { next ->
        val r = 0
        val c = width - 1
        place(next, r, c, jigsaw, used, logger)
        orient(orientations, jigsaw, r, c, matches[next]!!, logger)
    }
    // third corner
    cornerTiles.first { jigsaw[width - 2][0] in matches[it]!!.values && it !in used }.let { next ->
        val r = width - 1
        val c = 0
        place(next, r, c, jigsaw, used, logger)
        orient(orientations, jigsaw, r, c, matches[next]!!, logger)
    }
    // third edge
    (1 until width - 1).forEach { r ->
        val c = width - 1
        val prev = jigsaw[r - 1][c]
        val next = matches[prev]!!.values.first { it in edgeTiles && it !in used }
        place(next, r, c, jigsaw, used, logger)
        orient(orientations, jigsaw, r, c, matches[next]!!, logger)
    }
    // fourth edge
    (1 until width - 1).forEach { c ->
        val r = width - 1
        val prev = jigsaw[r][c - 1]
        val next = matches[prev]!!.values.first { it in edgeTiles && it !in used }
        place(next, r, c, jigsaw, used, logger)
        orient(orientations, jigsaw, r, c, matches[next]!!, logger)
    }
    // fourth corner
    cornerTiles.first { jigsaw[width - 2][width - 1] in matches[it]!!.values && it !in used }.let { next ->
        val c = width - 1
        val r = width - 1
        place(next, r, c, jigsaw, used, logger)
        orient(orientations, jigsaw, r, c, matches[next]!!, logger)
    }

    //middle
    (1 until width - 1).forEach { r ->
        (1 until width - 1).forEach { c ->
            val onTop = jigsaw[r - 1][c]
            val onLeft = jigsaw[r][c - 1]
            val next =
                matches[onTop]!!.values.intersect(matches[onLeft]!!.values).first { it in insideTiles && it !in used }
            place(next, r, c, jigsaw, used, logger)
            orient(orientations, jigsaw, r, c, matches[next]!!, logger)
        }
    }
    return jigsaw to orientations
}

private suspend fun place(
    next: Long,
    row: Int,
    col: Int,
    jigsaw: Array<Array<Long>>,
    used: MutableSet<Long>,
    logger: Day20ProgressLogger
) {
    check(next !in used) { "$next already used" }
    jigsaw[row][col] = next
    used += next
    logger.tilePlaced(next, row, col)
}

private suspend fun orient(
    orientations: Array<Array<Orientation>>,
    jigsaw: Array<Array<Long>>,
    r: Int,
    c: Int,
    matches: Map<Edge, Long>,
    logger: Day20ProgressLogger
) {
    val actT = if (r - 1 in jigsaw.indices) jigsaw[r - 1][c] else null
    val actB = if (r + 1 in jigsaw.indices) jigsaw[r + 1][c] else null
    val actL = if (c - 1 in jigsaw[r].indices) jigsaw[r][c - 1] else null
    val actR = if (c + 1 in jigsaw[r].indices) jigsaw[r][c + 1] else null
    val expT = matches[Top]
    val expB = matches[Bottom]
    val expL = matches[Left]
    val expR = matches[Right]
    orientations[r][c] = when {
        test(actT to expT, actR to expR, actB to expB, actL to expL) -> Orientation(Top, false)
        test(actT to expT, actR to expL, actB to expB, actL to expR) -> Orientation(Top, true)
        test(actT to expR, actR to expB, actB to expL, actL to expT) -> Orientation(Right, false)
        test(actT to expL, actR to expB, actB to expR, actL to expT) -> Orientation(Right, true)
        test(actT to expB, actR to expL, actB to expT, actL to expR) -> Orientation(Bottom, false)
        test(actT to expB, actR to expR, actB to expT, actL to expL) -> Orientation(Bottom, true)
        test(actT to expL, actR to expT, actB to expR, actL to expB) -> Orientation(Left, false)
        test(actT to expR, actR to expT, actB to expL, actL to expB) -> Orientation(Left, true)
        else -> error("wtf")
    }.also {
        logger.tileOriented(jigsaw[r][c], it)
    }
}

private fun test(vararg pairs: Pair<Long?, Long?>) =
    pairs.all { (actual, expected) -> expected == actual || actual == 0L }
