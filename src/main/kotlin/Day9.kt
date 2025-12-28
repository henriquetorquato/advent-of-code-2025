import java.math.BigInteger
import kotlin.collections.plus
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day9 {
    fun solve(diagram: String): Pair<BigInteger, BigInteger>
        = Pair(solvePart1(diagram), solvePart2(diagram))

    fun solvePart2(diagram: String): BigInteger {
        val tiles = parseOrThrow(diagram)
        val segments = sequence {
            for (idx in 1..<tiles.size) {
                yield(Segment(tiles[idx - 1], tiles[idx]))
            }

            yield(Segment(tiles[tiles.size - 1], tiles[0]))
        }.toList()
        val shape = Shape(segments)

        var rectangles = listOf<Rectangle>()
        for ((idA, tileA) in tiles.withIndex()) {
            for (idB in (idA + 1)..<tiles.size) {
                val tileB = tiles[idB]
                val rect = Rectangle.from(tileA, tileB)

                if (!shape.completelyContains(rect)) {
                    continue
                }

                rectangles = rectangles + rect
            }
        }

        return rectangles.maxOf { it.area() }
    }

    fun solvePart1(diagram: String): BigInteger {
        val tiles = parseOrThrow(diagram)
        var areas = listOf<Triple<Int, Int, BigInteger>>()

        for ((idA, tileA) in tiles.withIndex()) {
            for (idB in (idA + 1)..<tiles.size) {
                val tileB = tiles[idB]
                val rectangle = Rectangle.from(tileA, tileB)
                val area = Triple(idA, idB, rectangle.area())
                areas = areas + area
            }
        }

        return areas.maxOf { it.third }
    }

    private fun parseOrThrow(diagram: String): Array<Point> {
        var positions = listOf<Point>()

        for (line in diagram.lines()) {
            val pieces = line.split(',', limit = 2)

            if (pieces.size != 2) {
                throw RuntimeException("Invalid line $line")
            }

            positions = positions + Point(pieces[0].toInt(), pieces[1].toInt())
        }

        return positions.toTypedArray()
    }

    data class Point(
        val x: Int,
        val y: Int,
    )

    data class Segment(
        val a: Point,
        val b: Point,
    ) {
        val isVertical = a.x == b.x
        val minX = min(a.x, b.x)
        val maxX = max(a.x, b.x)
        val minY = min(a.y, b.y)
        val maxY = max(a.y, b.y)

        fun contains(point: Point): Boolean
            = if (isVertical) (
                point.x == a.x // Point on horizontal segment
                    && minY <= point.y
                    && maxY >= point.y
            ) else (
                point.y == a.y // Point on vertical segment
                    && minX <= point.x
                    && maxX >= point.x
            )

        fun pierce(rect: Rectangle): Boolean {
            val rectMinX = rect.bottomLeft.x
            val rectMaxX = rect.topRight.x
            val rectMinY = rect.bottomLeft.y
            val rectMaxY = rect.topRight.y

            return if (isVertical) (
                a.x in (rectMinX + 1)..<rectMaxX
                    && max(minY, rectMinY) < min(maxY, rectMaxY)
            ) else (
                a.y in (rectMinY + 1)..<rectMaxY
                    && max(minX, rectMinX) < min(maxX, rectMaxX)
            )
        }
    }

    data class Rectangle(
        val bottomLeft: Point,
        val topLeft: Point,
        val topRight: Point,
        val bottomRight: Point,
    ) {
       companion object {
           fun from(a: Point, b: Point): Rectangle {
               val minX = min(a.x, b.x)
               val maxX = max(a.x, b.x)
               val minY = min(a.y, b.y)
               val maxY = max(a.y, b.y)

               return Rectangle(
                   bottomLeft = Point(minX, minY),
                   topLeft = Point(minX, maxY),
                   topRight = Point(maxX, maxY),
                   bottomRight = Point(maxX, minY),
               )
           }
       }

        fun area(): BigInteger {
            val sideA = abs(topLeft.x - topRight.x) + 1
            val sideB = abs(topLeft.y - bottomLeft.y) + 1
            return sideA.toBigInteger().multiply(sideB.toBigInteger())
        }
    }

    data class Shape(
        val segments: List<Segment>
    ) {
        fun completelyContains(rect: Rectangle): Boolean {
            var isBottomLeftInside = false
            var isTopLeftInside = false
            var isTopRightInside = false
            var isBottomRightInside = false

            var bottomLeftCrossing = 0
            var topLeftCrossing = 0
            var topRightCrossing = 0
            var bottomRightCrossing = 0

            for (segment in segments) {
                // If any segment intersect the rectangle, then it's not inside
                if (segment.pierce(rect)) {
                    return false
                }

                if (!isBottomLeftInside) {
                    isBottomLeftInside = segment.contains(rect.bottomLeft)
                }

                if (!isTopLeftInside) {
                    isTopLeftInside = segment.contains(rect.topLeft)
                }

                if (!isTopRightInside) {
                    isTopRightInside = segment.contains(rect.topRight)
                }

                if (!isBottomRightInside) {
                    isBottomRightInside = segment.contains(rect.bottomRight)
                }

                if (!segment.isVertical) {
                    continue
                }

                val (a, b) = segment
                val edgeX = a.x
                val lowBound = min(a.y, b.y)
                val highBound = max(a.y, b.y)

                fun pointIntersects(point: Point): Boolean
                    = point.x < edgeX && point.y >= lowBound && point.y < highBound

                if (!isBottomLeftInside && pointIntersects(rect.bottomLeft)) {
                    bottomLeftCrossing += 1
                }

                if (!isTopLeftInside && pointIntersects(rect.topLeft)) {
                    topLeftCrossing += 1
                }

                if (!isTopRightInside && pointIntersects(rect.topRight)) {
                    topRightCrossing += 1
                }

                if (!isBottomRightInside && pointIntersects(rect.bottomRight)) {
                    bottomRightCrossing += 1
                }
            }

            isBottomLeftInside = isBottomLeftInside || bottomLeftCrossing.isOdd()
            isTopLeftInside = isTopLeftInside || topLeftCrossing.isOdd()
            isTopRightInside = isTopRightInside || topRightCrossing.isOdd()
            isBottomRightInside = isBottomRightInside || bottomRightCrossing.isOdd()

            return isBottomLeftInside && isTopLeftInside && isTopRightInside && isBottomRightInside
        }
    }
}

fun Int.isOdd() = this % 2 != 0