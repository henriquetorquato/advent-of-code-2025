import java.math.BigInteger
import kotlin.math.abs

class Day9 {
    fun solve(diagram: String): BigInteger {
        val tiles = parseOrThrow(diagram)
        var areas = listOf<Triple<Int, Int, BigInteger>>()

        for ((idA, tileA) in tiles.withIndex()) {
            for (idB in (idA + 1)..<tiles.size) {
                val tileB = tiles[idB]
                val area = Triple(idA, idB, tileA.rectangleArea(tileB))
                areas = areas + area
            }
        }

        return areas.maxOf { it.third }
    }

    private fun parseOrThrow(diagram: String): Array<Position> {
        var positions = listOf<Position>()

        for (line in diagram.lines()) {
            val pieces = line.split(',', limit = 2)

            if (pieces.size != 2) {
                throw RuntimeException("Invalid line $line")
            }

            positions = positions + Position(pieces[0].toInt(), pieces[1].toInt())
        }

        return positions.toTypedArray()
    }

    private data class Position(
        val x: Int,
        val y: Int,
    ) {
        fun rectangleArea(other: Position): BigInteger {
            val sideA = abs(x - other.x) + 1 // + 1 compensates for how the tiles are drawn on the diagram
            val sideB = abs(y - other.y) + 1
            return sideA.toBigInteger().multiply(sideB.toBigInteger())
        }
    }
}
