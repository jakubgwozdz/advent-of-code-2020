package advent2020.utils

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

suspend fun List<String>.linesAsFlowOfLong() = asSequence().asFlow().map { it.toLong() }

