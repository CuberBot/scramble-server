package net.lz1998.puzzle

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.worldcubeassociation.tnoodle.puzzle.*
import org.worldcubeassociation.tnoodle.scrambles.Puzzle

val log: Logger = LoggerFactory.getLogger(Puzzles::class.java)
val processorNum = Runtime.getRuntime().availableProcessors()
val coroutineContext = newFixedThreadPoolContext(
    1.coerceAtLeast(processorNum - 1),
    "scramble-producer"
)

class MyCubePuzzle(val size: Int) : CubePuzzle(size) {
    override fun getRandomMoveCount(): Int {
        return if (size < 12) super.getRandomMoveCount() else (20 * size - 40)
    }
}

enum class Puzzles(val puzzle: Puzzle) {
    TWO(TwoByTwoCubePuzzle()),
    THREE(ThreeByThreeCubePuzzle()),
    FOUR(FourByFourCubePuzzle()),
    FOUR_FAST(FourByFourRandomTurnsCubePuzzle()),
    FIVE(MyCubePuzzle(5)),
    SIX(MyCubePuzzle(6)),
    SEVEN(MyCubePuzzle(7)),
    EIGHT(MyCubePuzzle(8)),
    NINE(MyCubePuzzle(9)),
    TEN(MyCubePuzzle(10)),
    ELEVEN(MyCubePuzzle(11)),
    TWELVE(MyCubePuzzle(12)),
    THIRTEEN(MyCubePuzzle(13)),
    FOURTEEN(MyCubePuzzle(14)),
    FIFTEEN(MyCubePuzzle(15)),
    SIXTEEN(MyCubePuzzle(16)),
    SEVENTEEN(MyCubePuzzle(17)),
    THREE_NI(NoInspectionThreeByThreeCubePuzzle()),
    FOUR_NI(NoInspectionFourByFourCubePuzzle()),
    FIVE_NI(NoInspectionFiveByFiveCubePuzzle()),
    THREE_FM(ThreeByThreeCubeFewestMovesPuzzle()),
    PYRA(PyraminxPuzzle()),
    SQ1(SquareOnePuzzle()),
    MEGA(MegaminxPuzzle()),
    CLOCK(ClockPuzzle()),
    SKEWB(SkewbPuzzle());

    private val cacheQueue: Channel<String> = if (puzzle is MyCubePuzzle && puzzle.size > 3) Channel(5) else Channel(20)
    val shortName: String = puzzle.shortName

    init {
        CoroutineScope(coroutineContext).launch {
            while (true) {
                cacheQueue.send(puzzle.generateScramble())
                log.info("produce scramble ${puzzle.shortName}")
                delay(10)
            }
        }
    }

    suspend fun generateScramble(): String {
        val scramble = cacheQueue.receive()
        log.info("consume scramble ${puzzle.shortName}")
        return scramble
    }
}
