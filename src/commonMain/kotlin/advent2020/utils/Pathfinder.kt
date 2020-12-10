package advent2020.utils

/**
 * T - location
 * D - exits
 * R - state
 */
interface Pathfinder<T : Any, R : Any> {
    fun findShortest(startState: R, endOp: (R) -> Boolean): R?
}

open class BFSPathfinder<T : Any, R : Any, I : Comparable<I>>(
    val loggingOp: (() -> Any) -> Unit = {},
    val adderOp: (R, T) -> R,
    val distanceOp: ((R) -> I),
    val meaningfulOp: (R, I) -> Boolean = { _, _ -> true },
    val priority: Comparator<Pair<R, I>> = compareBy { it.second },
    val waysOutOp: (R) -> Iterable<T>,
) : Pathfinder<T, R> {

//   override fun findShortest(startState: R, end: T): R? = findShortest(startState) { _, t -> t == end }

    override fun findShortest(startState: R, endOp: (R) -> Boolean): R? {
        add(startState)
        while (toVisit.isNotEmpty()) {
            val state = pick()
            waysOutOp(state)
                .also { loggingOp { "WaysOut for $state: $it" } }
                .map { next -> adderOp(state, next) }
                .forEach { r ->
                    if (endOp(r)) {
                        done(r)
                    } else {
                        add(r)
                    }
                }
        }

        return currentBest?.first
    }

    private fun add(nextState: R) {
        val distance = distanceOp(nextState)
        if (!meaningfulOp(nextState, distance)) {
            loggingOp { "skipping $nextState with distance $distance, it's not meaningful" }
            return
        }
        val c = currentBest
        if (c == null || c.second > distance) {
            val new = nextState to distance
            toVisit.offer(new)
            loggingOp { "adding $nextState with distance $distance" }
        } else loggingOp { "skipping $nextState with distance $distance, we got better result already" }
    }

    private fun done(nextState: R) {
        val distance = distanceOp(nextState)
        val c = currentBest
        if (c == null || c.second > distance) {
            currentBest = nextState to distance
            loggingOp { "FOUND $nextState with distance $distance" }
        } else loggingOp { "skipping found $nextState with distance $distance, we got better result already" }
    }

    private fun pick(): R {
        val (r, i) = toVisit.poll()
        return r
    }

    private var currentBest: Pair<R, I>? = null
    private val toVisit = Queue<Pair<R, I>>()
    // private val toVisit = PriorityQueue<Pair<R, I>>(priority)
//    private val toVisit: MutableList<Triple<T, R, I>> = mutableListOf()

}

class BasicPathfinder<T : Any, I : Comparable<I>>(
    loggingOp: (() -> Any) -> Unit = {},
    adderOp: (List<T>, T) -> List<T> = { l, t -> l + t },
    distanceOp: ((List<T>) -> I),
    waysOutOp: (List<T>) -> Iterable<T>,
    private val cache: Cache<T, I> = Cache(),
) : BFSPathfinder<T, List<T>, I>(
    loggingOp = loggingOp,
    adderOp = adderOp,
    distanceOp = distanceOp,
    waysOutOp = { l -> waysOutOp(l).filter { it !in l } },
    meaningfulOp = { l, d -> cache.isBetterThanPrevious(l.last(), d) }
)

class Cache<R : Any, I : Comparable<I>> {
    private val cache = mutableMapOf<R, I>()
    fun isBetterThanPrevious(state: R, distance: I): Boolean {
        val previous = cache[state]
        return when {
            previous != null && previous <= distance -> {
                false
            }
            else -> {
                cache[state] = distance
                true
            }
        }
    }
}
