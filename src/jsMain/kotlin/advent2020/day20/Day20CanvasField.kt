package advent2020.day20

import advent2020.day20.Edge.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.DOMMatrix
import org.w3c.dom.DOMPoint
import org.w3c.dom.HTMLCanvasElement

class Day20CanvasField(val canvas: HTMLCanvasElement) {

    object Colors {
        const val black = "rgb(31,36,36)"
        const val border = "rgb(200,200,255)"
        const val background = "rgb(75, 101, 171)"
        const val tileBg = "rgb(34, 85, 221)"
        const val text = "rgb(255, 255, 255)"
        const val imagePixel = "rgb(221, 238, 255)"
        const val monsterPixel = "rgb(255, 0, 0)"
        const val jigsawPixel = "rgb(255, 176, 121)"
        const val jigsawPixelNoMatch = "rgb(0, 0, 0)"
        const val connection = "rgb(255, 0, 0)"
    }


    var size: Int
        get() = canvas.width
        set(value) {
            canvas.width = value
            canvas.height = value
        }

    fun drawImage(
        image: Tile,
        imageAngle: Double,
        imageFlip: Double,
        monsters: Set<Pair<Int, Int>>,
        currentMonster: Set<Pair<Int, Int>>,
        currentMonsterPct: Double
    ) {
        val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
        ctx.save()
        val w = ctx.canvas.width
        ctx.scale(w / 2400.0, w / 2400.0)

        ctx.fillStyle = Colors.black
        ctx.fillRect(0.0, 0.0, 2400.0, 2400.0)

        val ts = 2400.0 / image.lines.size
        val transform = DOMMatrix()
            .translate(2400.0 / 2, 2400.0 / 2)
            .rotate(imageAngle)
            .multiply(DOMMatrix(arrayOf(imageFlip, 0.0, 0.0, 1.0, 0.0, 0.0)))
            .translate(-2400.0 / 2, -2400.0 / 2)

        ctx.setTransform(ctx.getTransform().multiply(transform))

        ctx.fillStyle = Colors.border
        ctx.fillRect(0.0, 0.0, 2400.0, 2400.0)
        ctx.fillStyle = Colors.background
        ctx.fillRect(4.0, 4.0, 2392.0, 2392.0)

        ctx.fillStyle = Colors.imagePixel
        image.lines.indices.forEach { y ->
            image.lines[y].indices.forEach { x ->
                if (image.lines[y][x] == '#') {
                    ctx.fillRect(x * ts, y * ts, ts, ts)
                }
            }
        }

        drawMonsters(ctx, monsters, ts)

        drawMonsters(ctx, currentMonster, ts, currentMonsterPct)

        ctx.restore()
    }

    private fun drawMonsters(
        ctx: CanvasRenderingContext2D,
        monsters: Set<Pair<Int, Int>>,
        ts: Double,
        alpha: Double = 1.0
    ) {
        ctx.fillStyle = Colors.monsterPixel
        ctx.globalAlpha = alpha
        monsters.forEach { (y, x) ->
            ctx.fillRect(x * ts - 10, y * ts - 10, ts + 20, ts + 20)
        }

        ctx.globalAlpha = 1 - (alpha / 2)
        ctx.fillStyle = Colors.imagePixel
        monsters.forEach { (y, x) ->
            ctx.fillRect(x * ts, y * ts, ts, ts)
        }
    }


    fun drawTiles(tiles: Map<Long, TileInfo>, highlighted: Collection<Long> = emptySet(), scale: Double = 0.8) {
        val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
        ctx.save()
        val w = ctx.canvas.width
        ctx.scale(w / 2400.0, w / 2400.0)

        ctx.fillStyle = Colors.border
        ctx.fillRect(0.0, 0.0, 2400.0, 2400.0)
        ctx.fillStyle = Colors.background
        ctx.fillRect(4.0, 4.0, 2392.0, 2392.0)

        tiles.values
            .filter { tileInfo -> !tileInfo.correctPlace }
            .forEach { tileInfo ->
                drawConnections(
                    ctx,
                    tileInfo,
                    if (tileInfo.tile.id in highlighted) 1.0 else 0.2,
                    tileInfo.matches.map { tiles[it.value]!! })

                // go back to original values
            }

        // tiles
        tiles.values
            .forEach { tileInfo ->
                val alpha = when {
                    tileInfo.tile.id in highlighted -> 1.0
                    tileInfo.matches.values.any { it in highlighted } -> 1.0
                    tileInfo.matches.count() == 2 -> 0.9
                    tileInfo.correctPlace -> 0.8
                    else -> 0.3
                }
                drawTile(ctx, tileInfo, alpha, scale)

                // go back to original values
            }

        ctx.restore()
    }

    private fun drawTile(ctx: CanvasRenderingContext2D, tileInfo: TileInfo, alpha: Double, scale: Double = 0.8) {

        val origTransform = ctx.getTransform()
        ctx.setTransform(origTransform.multiply(tileTransform(tileInfo, scale = scale)))

        val tile = tileInfo.tile

        val ts = 20.0 // tile size

        val aFg = (1.0 - alpha) * (scale - 0.8) / (0.45) + alpha
        val aBg = (-alpha) * (scale - 0.8) / (0.45) + alpha

        ctx.globalAlpha = aBg * (1.0 / tileInfo.scale)
        ctx.fillStyle = Colors.border
        ctx.fillRect(-2.0, -2.0, 204.0, 204.0)
        ctx.fillStyle = Colors.tileBg
        ctx.fillRect(0.0, 0.0, 200.0, 200.0)

        ctx.strokeStyle = Colors.border
        ctx.beginPath()
        ctx.moveTo(0.0, 0.0)
        ctx.lineTo(5.0, -30.0)
        ctx.lineTo(90.0, -30.0)
        ctx.lineTo(100.0, 0.0)
        ctx.stroke()

        ctx.fillStyle = Colors.text
        ctx.font = "30px Lato"
        ctx.fillText("${tile.id}", 10.0, -5.0)

        ctx.globalAlpha = aFg * (1.0 / tileInfo.scale)
        ctx.fillStyle = Colors.imagePixel
        (1..tile.size - 2).forEach { y ->
            (1..tile.size - 2).forEach { x ->
                if (tile.at(y, x) == '#')
                    ctx.fillRect(x * ts, y * ts, ts, ts)
            }
        }

        ctx.globalAlpha = aBg * (1.0 / tileInfo.scale)
        // edges
        val hasT = Top in tileInfo.matches
        val hasR = Right in tileInfo.matches
        val hasB = Bottom in tileInfo.matches
        val hasL = Left in tileInfo.matches

        borderPixels(tile.size)
            .filter { (x, y) -> tile.at(y, x) == '#' }
            .forEach { (x, y) ->
                if (x == 0 && hasL || x == tile.size - 1 && hasR || y == 0 && hasT || y == tile.size - 1 && hasB)
                    ctx.fillStyle = Colors.jigsawPixel
                else
                    ctx.fillStyle = Colors.jigsawPixelNoMatch
                ctx.fillRect(x * ts, y * ts, ts, ts)
            }


//        ctx.fillStyle = Colors.text
//        ctx.font = "24px Lato"
//        tileInfo.matches.forEach { (edge, id) ->
//            val t = ctx.getTransform()
//            ctx.translate(100.0, 100.0)
//            when (edge) {
//                Top -> Unit
//                Right -> ctx.rotate(PI / 2)
//                Bottom -> ctx.rotate(PI)
//                Left -> ctx.rotate(3 * PI / 2)
//            }
//            val v = ctx.measureText("$id").width
//            ctx.fillText("$id", -v / 2, -80.0)
//
//            ctx.setTransform(t)
//        }
        ctx.setTransform(origTransform)
    }

    private val borderPixelsCache = mutableMapOf<Int, Collection<Pair<Int, Int>>>()
    private fun borderPixels(size: Int): Collection<Pair<Int, Int>> {
        return borderPixelsCache.getOrPut(size) {
            (0 until size)
                .flatMap { listOf(0 to it, size - 1 to it, it to 0, it to size - 1) }
                .toSet()
        }
    }


    private fun drawConnections(
        ctx: CanvasRenderingContext2D,
        tileInfo: TileInfo,
        alpha: Double,
        connected: List<TileInfo>,
    ) {
        ctx.globalAlpha = alpha
        ctx.strokeStyle = Colors.connection
        ctx.lineWidth = 5.0
        val tr1 = tileTransform(tileInfo)
        connected.forEach { other ->
            val tr2 = tileTransform(other)
            val start = DOMPoint(100.0, 100.0).matrixTransform(tr1)
            val end = DOMPoint(100.0, 100.0).matrixTransform(tr2)
            ctx.beginPath()
            ctx.moveTo(start.x, start.y)
            ctx.lineTo(end.x, end.y)
            ctx.stroke()
        }
    }

    private fun tileTransform(tileInfo: TileInfo, size: Double = 200.0, scale: Double = 0.8) = DOMMatrix()
        .translate(tileInfo.column * size, tileInfo.row * size)
        .translate(size / 2, size / 2)
        .scale(scale)
        .rotate(tileInfo.angle)
        .multiply(DOMMatrix(arrayOf(tileInfo.flip, 0.0, 0.0, 1.0, 0.0, 0.0)))
        .scale(tileInfo.scale)
        .translate(-size / 2, -size / 2)

}
