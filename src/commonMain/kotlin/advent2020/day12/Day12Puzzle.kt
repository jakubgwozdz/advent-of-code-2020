package advent2020.day12

import advent2020.day12.Action.*
import kotlin.math.absoluteValue

enum class Action { N, S, E, W, L, R, F }

data class Command(val action: Action, val count: Int) {
    constructor(s: String) : this(Action.valueOf(s.take(1)), s.drop(1).toInt())
}

internal fun parseAsSequence(input: String) =
    input.trim().lineSequence().map(::Command)

data class Vector(val dx: Int, val dy: Int) {
    operator fun plus(that: Vector) = Vector(dx + that.dx, dy + that.dy)
    operator fun times(count: Int) = Vector(dx * count, dy * count)
    operator fun unaryMinus() = Vector(-dx, -dy)
    fun rotateLeft(count: Int) = when (count % 360) {
        0 -> this
        90 -> Vector(-dy, dx)
        180 -> -this
        270 -> Vector(dy, -dx)
        else -> error("can't rotate $count")
    }
    fun rotateRight(count: Int) = when (count % 360) {
        0 -> this
        90 -> Vector(dy, -dx)
        180 -> -this
        270 -> Vector(-dy, dx)
        else -> error("can't rotate $count")
    }
}

data class Position(val x: Int, val y: Int) {
    operator fun plus(v: Vector) = Position(x + v.dx, y + v.dy)
    val manhattan get() = x.absoluteValue + y.absoluteValue
}

fun direction(action: Action) = when (action) {
    N -> Vector(0, 1)
    S -> Vector(0, -1)
    E -> Vector(1, 0)
    W -> Vector(-1, 0)
    else -> error("`$action` is not a direction")
}

data class State(val position: Position, val waypoint: Vector)

fun State.part1Command(command: Command) = when (command.action) {
    N, S, E, W -> copy(position = position + direction(command.action) * command.count)
    L -> copy(waypoint = waypoint.rotateLeft(command.count))
    R -> copy(waypoint = waypoint.rotateRight(command.count))
    F -> copy(position = position + waypoint * command.count)
}

fun part1(input: String): String {
    val commands = parseAsSequence(input)

    val start = State(Position(0, 0), Vector(1, 0))
    val end = commands.fold(start, State::part1Command)
    val manhattan = end.position.manhattan

    return manhattan.toString()
}

fun State.part2Command(command: Command) = when (command.action) {
    N, S, E, W -> copy(waypoint = waypoint + direction(command.action) * command.count)
    L -> copy(waypoint = waypoint.rotateLeft(command.count))
    R -> copy(waypoint = waypoint.rotateRight(command.count))
    F -> copy(position = position + waypoint * command.count)
}

fun part2(input: String): String {
    val commands = parseAsSequence(input)

    val start = State(Position(0, 0), Vector(10, 1))
    val end = commands.fold(start, State::part2Command)
    val manhattan = end.position.manhattan
    return manhattan.toString()
}


