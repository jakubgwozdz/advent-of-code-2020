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
        const val text = "rgb(0, 0, 0)"
        const val imagePixel = "rgb(221, 238, 255)"
        const val monsterPixel = "rgb(255, 0, 0)"
        const val jigsawPixel = "rgb(255, 176, 121)"
        const val jigsawPixelNoMatch = "rgb(0, 0, 0)"
        const val connection = "rgb(255, 0, 0)"
    }

    var logicalWidth = 1200.0
    var tileWidth = logicalWidth / 12
    var pixelWidth = tileWidth / 10

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
        ctx.scale(w / logicalWidth, w / logicalWidth)

        ctx.fillStyle = Colors.black
        ctx.fillRect(0.0, 0.0, logicalWidth, logicalWidth)

        pixelWidth = logicalWidth / image.lines.size
        val transform = DOMMatrix()
            .translate(logicalWidth / 2, logicalWidth / 2)
            .rotate(imageAngle)
            .scale(imageFlip, 1.0)
            .translate(-logicalWidth / 2, -logicalWidth / 2)

        ctx.setTransform(ctx.getTransform().multiply(transform))

        drawTable(ctx)

        ctx.fillStyle = Colors.imagePixel
        image.lines.indices.forEach { y ->
            image.lines[y].indices.forEach { x ->
                if (image.lines[y][x] == '#') {
                    ctx.fillRect(x * pixelWidth, y * pixelWidth, pixelWidth, pixelWidth)
                }
            }
        }

        drawMonsters(ctx, monsters)

        drawMonsters(ctx, currentMonster, currentMonsterPct)

        ctx.restore()
    }

    private fun drawMonsters(
        ctx: CanvasRenderingContext2D,
        monsters: Set<Pair<Int, Int>>,
        alpha: Double = 1.0
    ) {
        ctx.fillStyle = Colors.monsterPixel
        ctx.globalAlpha = alpha
        monsters.forEach { (y, x) ->
            ctx.fillRect((x - 0.5) * pixelWidth, (y - 0.5) * pixelWidth, pixelWidth * 2, pixelWidth * 2)
        }

        ctx.globalAlpha = 1 - (alpha / 2)
        ctx.fillStyle = Colors.imagePixel
        monsters.forEach { (y, x) ->
            ctx.fillRect(x * pixelWidth, y * pixelWidth, pixelWidth, pixelWidth)
        }
    }


    fun drawTiles(tiles: Map<Long, TileInfo>, highlighted: Collection<Long> = emptySet(), scale: Double = 0.8) {
        val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
        ctx.save()
        val w = ctx.canvas.width
        ctx.scale(w / logicalWidth, w / logicalWidth)

        if (tiles.isNotEmpty()) {
            tileWidth = logicalWidth / 12 // 12 is hardcoded
            pixelWidth = tileWidth / tiles.values.first().tile.lines.size
        }
        drawTable(ctx)


        // tiles
        tiles.values
            .forEach { tileInfo ->
                val alpha = when {
                    tileInfo.tile.id in highlighted -> 1.0
                    tileInfo.matches.values.any { it in highlighted } -> 1.0
                    tileInfo.matches.count() == 2 -> 0.9
                    tileInfo.correctPlace -> 0.8
                    else -> 0.6
                }
                drawTile(ctx, tileInfo, alpha, scale)

            }

        tiles.values
            .filter { tileInfo -> !tileInfo.correctPlace }
            .forEach { tileInfo ->
                drawConnections(
                    ctx,
                    tileInfo,
                    if (tileInfo.tile.id in highlighted) 1.0 else 0.2,
                    tileInfo.matches.map { tiles[it.value]!! })

            }

        ctx.restore()
    }

    private fun drawTable(ctx: CanvasRenderingContext2D) {
        val borderWidth = pixelWidth / 5
        ctx.fillStyle = Colors.border
        ctx.fillRect(0.0, 0.0, logicalWidth, logicalWidth)
        ctx.fillStyle = Colors.background
        ctx.fillRect(borderWidth, borderWidth, logicalWidth - 2 * borderWidth, logicalWidth - 2 * borderWidth)
    }

    private fun drawTile(ctx: CanvasRenderingContext2D, tileInfo: TileInfo, alpha: Double, scale: Double = 0.8) {

        val origTransform = ctx.getTransform()
        ctx.setTransform(origTransform.multiply(tileTransform(tileInfo, scale = scale)))

        val tile = tileInfo.tile

        val aFg = (1.0 - alpha) * (scale - 0.8) / (0.45) + alpha
        val aBg = (-alpha) * (scale - 0.8) / (0.45) + alpha

        val borderWidth = tileWidth / 100
        ctx.globalAlpha = aBg * (1.0 / tileInfo.scale)
        ctx.fillStyle = Colors.border
        ctx.fillRect(-borderWidth, -borderWidth, tileWidth + 2 * borderWidth, tileWidth + 2 * borderWidth)
        ctx.fillStyle = Colors.tileBg
        ctx.fillRect(0.0, 0.0, tileWidth, tileWidth)

        ctx.fillStyle = Colors.border
        ctx.beginPath()
        ctx.moveTo(0.0, 0.0)
        ctx.lineTo(5.0 * tileWidth / 200, -30.0 * tileWidth / 200)
        ctx.lineTo(90.0 * tileWidth / 200, -30.0 * tileWidth / 200)
        ctx.lineTo(100.0 * tileWidth / 200, 0.0)
        ctx.fill()

        ctx.strokeStyle = Colors.text
        ctx.fillStyle = Colors.text
        val fontSize = 30 * tileWidth / 200
        ctx.font = "${fontSize.toInt()}px Lato"
        ctx.fillText("${tile.id}", 10.0 * tileWidth / 200, -5.0 * tileWidth / 200)

        ctx.globalAlpha = aFg * (1.0 / tileInfo.scale)
        ctx.fillStyle = Colors.imagePixel
        (1..tile.size - 2).forEach { y ->
            (1..tile.size - 2).forEach { x ->
                if (tile.at(y, x) == '#')
                    ctx.fillRect(x * pixelWidth, y * pixelWidth, pixelWidth, pixelWidth)
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
                ctx.fillRect(x * pixelWidth, y * pixelWidth, pixelWidth, pixelWidth)
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
        ctx.lineWidth = 5.0 * tileWidth / 100
        val tr1 = tileTransform(tileInfo)
        connected.forEach { other ->
            val tr2 = tileTransform(other)
            val start = DOMPoint(tileWidth / 2, tileWidth / 2).matrixTransform(tr1)
            val end = DOMPoint(tileWidth / 2, tileWidth / 2).matrixTransform(tr2)
            ctx.beginPath()
            ctx.moveTo(start.x, start.y)
            ctx.lineTo(end.x, end.y)
            ctx.stroke()
        }
    }

//    private val tileTransformCache = mutableMapOf<>()

    private fun tileTransform(tileInfo: TileInfo, scale: Double = 0.8) = DOMMatrix()
        .translate(tileInfo.column * tileWidth, tileInfo.row * tileWidth)
        .translate(tileWidth / 2, tileWidth / 2)
        .scale(scale)
        .rotate(tileInfo.angle)
//        .multiply(DOMMatrix(arrayOf(tileInfo.flip, 0.0, 0.0, 1.0, 0.0, 0.0)))
        .scale(tileInfo.scale * tileInfo.flip, tileInfo.scale)
        .translate(-tileWidth / 2, -tileWidth / 2)

}
