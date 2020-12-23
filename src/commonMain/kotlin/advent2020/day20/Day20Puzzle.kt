package advent2020.day20

import advent2020.ProgressLogger
import advent2020.utils.groups

enum class Edge { top, right, bottom, left }
data class Orientation(val topEdge: Edge, val flip: Boolean)

fun List<Char>.asInt() = map { if (it == '#') "1" else "0" }.joinToString("").toInt(2)

data class Tile(val id: Long, val lines: List<String>) {
    val size = lines.count()
    private val top = lines.first().toList().asInt()
    private val topR = lines.first().toList().reversed().asInt()

    private val right = lines.map { it.last() }.asInt()
    private val rightR = lines.map { it.last() }.reversed().asInt()

    private val bottomR = lines.last().toList().asInt()
    private val bottom = lines.last().toList().reversed().asInt()

    private val leftR = lines.map { it.first() }.asInt()
    private val left = lines.map { it.first() }.reversed().asInt()

    private val edgesCodes = listOf(top, right, bottom, left, topR, rightR, bottomR, leftR)

    fun at(y: Int, x: Int, o: Orientation = Orientation(Edge.top, false)): Char {
        val (topEdge, flip) = o
        return when {
            x !in lines.indices -> ' '
            y !in lines.indices -> ' '
            topEdge == Edge.top && !flip -> lines[y][x]
            topEdge == Edge.top && flip -> lines[y][size - 1 - x]
            topEdge == Edge.right && flip -> lines[x][y]
            topEdge == Edge.right && !flip -> lines[x][size - 1 - y]
            topEdge == Edge.bottom && !flip -> lines[size - 1 - y][size - 1 - x]
            topEdge == Edge.bottom && flip -> lines[size - 1 - y][x]
            topEdge == Edge.left && !flip -> lines[size - 1 - x][y]
            topEdge == Edge.left && flip -> lines[size - 1 - x][size - 1 - y]
            else -> error("unknown $o")
        }
    }

    fun match(o: Tile) = when {
        top in o.edgesCodes -> Edge.top
        right in o.edgesCodes -> Edge.right
        bottom in o.edgesCodes -> Edge.bottom
        left in o.edgesCodes -> Edge.left
        else -> null
    }
}

interface Day20ProgressLogger : ProgressLogger {
    suspend fun foundTiles(tiles: Map<Long, Tile>) {}
}

suspend fun part1(input: String, logger: ProgressLogger = object : Day20ProgressLogger {}): String {
    logger as Day20ProgressLogger

    val tiles = tiles(input)

    logger.foundTiles(tiles)

    val matches = matches(tiles)

    val result = matches.filterValues { it.size == 2 }
        .keys.fold(1L) { a, i -> a * i }

    return result.toString()
}

private fun matches(tiles: Map<Long, Tile>) = tiles.map { (id1, t1) ->
    id1 to tiles
        .filter { (id2, _) -> id1 != id2 }
        .map { (id2, t2) -> t1.match(t2)?.let { it to id2 } }
        .filterNotNull()
        .toMap()
}.toMap()

private fun tiles(input: String) = input.trim().lineSequence()
    .groups { it.isBlank() }
    .map {
        val id = """Tile (\d+):""".toRegex().matchEntire(it[0])?.destructured?.component1()?.toLong()
            ?: error("not id @ ${it[0]}")
        Tile(id, it.drop(1))
    }.toList().associateBy { it.id }

fun part2(input: String): String {
    val tiles = tiles(input)
    val matches = matches(tiles)

    val byNeighbours = matches.entries.groupBy { (_, m) -> m.size }
        .mapValues { (_, v) -> v.map { it.key } }
    val cornerTiles = byNeighbours[2]!!
    val edgeTiles = byNeighbours[3]!!
    val insideTiles = byNeighbours[4]!!

    val width = edgeTiles.size / 4 + 2
    val jigsaw: Array<Array<Long?>> = Array(width) { Array(width) { null } }
    val used = mutableSetOf<Long>()

    // first corner
    cornerTiles.first().let { next ->
        place(next, 0, 0, jigsaw, used)
    }
    // first of first edge
    (1).let { c ->
        val prev = jigsaw[0][c - 1]!!
        val next = matches[prev]!!.values.first { it in edgeTiles && it !in used }
        place(next, 0, c, jigsaw, used)
    }
    // rest of first edge
    (2 until width - 1).forEach { c ->
        val prev = jigsaw[0][c - 1]!!
        val next = matches[prev]!!.values.single { it in edgeTiles && it !in used }
        place(next, 0, c, jigsaw, used)
    }
    // second edge
    (1 until width - 1).forEach { r ->
        val prev = jigsaw[r - 1][0]!!
        val next = matches[prev]!!.values.single { it in edgeTiles && it !in used }
        place(next, r, 0, jigsaw, used)
    }
    // second corner
    cornerTiles.first { jigsaw[0][width - 2]!! in matches[it]!!.values && it !in used }.let { next ->
        place(next, 0, width - 1, jigsaw, used)
    }
    // third corner
    cornerTiles.first { jigsaw[width - 2][0]!! in matches[it]!!.values && it !in used }.let { next ->
        place(next, width - 1, 0, jigsaw, used)
    }
    // third edge
    (1 until width - 1).forEach { r ->
        val prev = jigsaw[r - 1][width - 1]!!
        val next = matches[prev]!!.values.single { it in edgeTiles && it !in used }
        place(next, r, width - 1, jigsaw, used)
    }
    // fourth edge
    (1 until width - 1).forEach { c ->
        val prev = jigsaw[width - 1][c - 1]!!
        val next = matches[prev]!!.values.single { it in edgeTiles && it !in used }
        place(next, width - 1, c, jigsaw, used)
    }
    // fourth corner
    cornerTiles.first { jigsaw[width - 2][width - 1]!! in matches[it]!!.values && it !in used }.let { next ->
        place(next, width - 1, width - 1, jigsaw, used)
    }

    //middle
    (1 until width - 1).forEach { c ->
        (1 until width - 1).forEach { r ->
            val onTop = jigsaw[r - 1][c]!!
            val onLeft = jigsaw[r][c - 1]!!
            val next =
                matches[onTop]!!.values.intersect(matches[onLeft]!!.values).single { it in insideTiles && it !in used }
            place(next, r, c, jigsaw, used)
        }
    }

    val tileSize = 10
    val image = Array((tileSize - 1) * width + 1) { Array((tileSize - 1) * width + 1) { ' ' } }

    // place
    jigsaw.indices.forEach { r ->
        jigsaw[r].indices.forEach { c ->
            val tile = jigsaw[r][c]!!
            val actTop = if (r - 1 in jigsaw.indices) jigsaw[r - 1][c] else null
            val actBottom = if (r + 1 in jigsaw.indices) jigsaw[r + 1][c] else null
            val actLeft = if (c - 1 in jigsaw[r].indices) jigsaw[r][c - 1] else null
            val actRight = if (c + 1 in jigsaw[r].indices) jigsaw[r][c + 1] else null
            val m = matches[tile]!!
            val expTop = m[Edge.top]
            val expBottom = m[Edge.bottom]
            val expLeft = m[Edge.left]
            val expRight = m[Edge.right]
            val act = listOf(actTop, actRight, actBottom, actLeft)
            val orientation =
                Edge.values().flatMap { listOf(Orientation(it, false), Orientation(it, true)) }
                    .single { (t, flip) ->
                        when {
                            t == Edge.top && !flip -> act == listOf(expTop, expRight, expBottom, expLeft)
                            t == Edge.top && flip -> act == listOf(expTop, expLeft, expBottom, expRight)
                            t == Edge.right && !flip -> act == listOf(expRight, expBottom, expLeft, expTop)
                            t == Edge.right && flip -> act == listOf(expLeft, expBottom, expRight, expTop)
                            t == Edge.bottom && !flip -> act == listOf(expBottom, expLeft, expTop, expRight)
                            t == Edge.bottom && flip -> act == listOf(expBottom, expRight, expTop, expLeft)
                            t == Edge.left && !flip -> act == listOf(expLeft, expTop, expRight, expBottom)
                            t == Edge.left && flip -> act == listOf(expRight, expTop, expLeft, expBottom)
                            else -> error("wtf")
                        }
                    }

            (1 until tileSize - 1).forEach { y ->
                (1 until tileSize - 1).forEach { x ->
                    val at = tiles[tile]!!.at(y, x, orientation)
                    image[r * (tileSize - 2) + y][c * (tileSize - 2) + x] = at
                }
            }
        }
    }

    val full = Tile(0, image.map { line -> line.joinToString("") })

    val str = """
        |                  # 
        |#    ##    ##    ###
        | #  #  #  #  #  #   
        """.trimMargin()

    val monster = str.lines()
        .flatMapIndexed { y: Int, l: String -> l.mapIndexedNotNull { x, c -> if (c == '#') y to x else null } }

    val monsters = Edge.values().flatMap { listOf(Orientation(it, false), Orientation(it, true)) }
        .sumBy { o ->
            (0..image.size).sumBy { y ->
                (0..image.size).count { x ->
                    monster.all { (my, mx) -> full.at(y + my, x + mx, o) == '#' }
                }
            }
        }

    val hashes = image.sumBy { it.count { c -> c == '#' } }

    val result = hashes - monster.size * monsters

    return result.toString()
}

private fun place(next: Long, row: Int, col: Int, jigsaw: Array<Array<Long?>>, used: MutableSet<Long>) {
    check(next !in used) { "$next already used" }
    jigsaw[row][col] = next
    used += next
}

