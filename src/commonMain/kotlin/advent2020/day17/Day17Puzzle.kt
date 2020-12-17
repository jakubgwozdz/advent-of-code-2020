package advent2020.day17

data class Vector3(val dx: Int, val dy: Int, val dz: Int)
data class Pos3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(v: Vector3) = Pos3(x + v.dx, y + v.dy, z + v.dz)
}

data class Vector4(val dx: Int, val dy: Int, val dz: Int, val dw: Int)
data class Pos4(val x: Int, val y: Int, val z: Int, val w: Int) {
    operator fun plus(v: Vector4) = Pos4(x + v.dx, y + v.dy, z + v.dz, w + v.dw)
}

fun part1(input: String): String {
    val lines = input.trim().lines()
    var map =
        lines.flatMapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') Pos3(x, y, 0) else null } }

    repeat(6) {
        var minX = 0
        var minY = 0
        var minZ = 0
        var maxX = 0
        var maxY = 0
        var maxZ = 0
        map.forEach {
            if (minX > it.x) minX = it.x
            if (minY > it.y) minY = it.y
            if (minZ > it.z) minZ = it.z
            if (maxX < it.x) maxX = it.x
            if (maxY < it.y) maxY = it.y
            if (maxZ < it.z) maxZ = it.z
        }
        val m2 = (minX - 1..maxX + 2).flatMap { x ->
            (minY - 1..maxY + 1).flatMap { y ->
                (minZ - 1..maxZ + 1).mapNotNull { z ->
                    val pos = Pos3(x, y, z)
                    val count = (-1..1).flatMap { dx ->
                        (-1..1).flatMap { dy ->
                            (-1..1).map { dz ->
                                Vector3(dx, dy, dz)
                            }
                        }
                    }
                        .filter { it != Vector3(0, 0, 0) }
                        .count { (pos + it) in map }
                    val prevState = pos in map
                    val nextState = if (prevState) {
                        (count == 2 || count == 3)
                    } else {
                        (count == 3)
                    }
                    if (nextState) pos else null
                }
            }
        }

        map = m2
    }

    return map.size.toString()

}

fun part2(input: String): String {
    val lines = input.trim().lines()
    var map =
        lines.flatMapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') Pos4(x, y, 0, 0) else null } }

    repeat(6) {
        var minX = 0
        var minY = 0
        var minZ = 0
        var minW = 0
        var maxX = 0
        var maxY = 0
        var maxZ = 0
        var maxW = 0
        map.forEach {
            if (minX > it.x) minX = it.x
            if (minY > it.y) minY = it.y
            if (minZ > it.z) minZ = it.z
            if (minW > it.w) minW = it.w
            if (maxX < it.x) maxX = it.x
            if (maxY < it.y) maxY = it.y
            if (maxZ < it.z) maxZ = it.z
            if (maxW < it.w) maxW = it.w
        }
        val m2 = (minX - 1..maxX + 2).flatMap { x ->
            (minW - 1..maxW + 1).flatMap { w ->
                (minY - 1..maxY + 1).flatMap { y ->
                    (minZ - 1..maxZ + 1).mapNotNull { z ->
                        val pos = Pos4(x, y, z, w)
                        val count = (-1..1).flatMap { dx ->
                            (-1..1).flatMap { dy ->
                                (-1..1).flatMap { dw ->
                                    (-1..1).map { dz ->
                                        Vector4(dx, dy, dz, dw)
                                    }
                                }
                            }
                        }
                            .filter { it != Vector4(0, 0, 0, 0) }
                            .count { (pos + it) in map }
                        val prevState = pos in map
                        val nextState = if (prevState) {
                            (count == 2 || count == 3)
                        } else {
                            (count == 3)
                        }
                        if (nextState) pos else null
                    }
                }
            }
        }

        map = m2
    }

    return map.size.toString()

}

