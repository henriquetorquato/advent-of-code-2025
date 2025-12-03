import java.math.BigInteger
import java.util.stream.Stream

class Day2 {
    fun solve(input: String): Pair<BigInteger, BigInteger> {
        val ranges = parseOrThrow(input)
        var totalPart1 = BigInteger.ZERO
        var totalPart2 = BigInteger.ZERO

        for ((from, to) in ranges) {
            val stream = Stream
                .iterate(from) { it.add(BigInteger.ONE) }
                .takeWhile { it <= to }

            for (id in stream) {
                if (!isIDValidPart1(id)) {
                    totalPart1 = totalPart1.add(id)
                }

                if (!isIDValidPart2(id)) {
                    totalPart2 = totalPart2.add(id)
                }
            }
        }

        return Pair(totalPart1, totalPart2)
    }

    fun isIDValidPart1(id: BigInteger): Boolean {
        val strId = id.toString()

        // Every odd length value is valid
        if (strId.length % 2 != 0) {
            return true
        }

        val limit = strId.length.floorDiv(2)
        val part1 = strId.substring(0..<limit)
        val part2 = strId.substring(limit..<strId.length)

        return part1 != part2
    }

    fun isIDValidPart2(id: BigInteger): Boolean {
        val strId = id.toString()
        val maxWindowSize = strId.length.floorDiv(2)

        for (windowSize in 1..maxWindowSize) {
            if (strId.length % windowSize != 0) {
                continue
            }

            val value = strId.take(windowSize)

            val repetitions = strId.windowed(
                size = windowSize,
                step = windowSize,
                partialWindows = true,
            )

            if (repetitions.all { it == value }) {
                return false
            }
        }

        return true
    }

    private fun parseOrThrow(input: String): List<Pair<BigInteger, BigInteger>> {
        return input
            .split(',')
            .map { range ->
                val parts = range.split('-')

                if (parts.size != 2) {
                    throw RuntimeException("Invalid range $range")
                }

                val from = parts[0]
                val to = parts[1]

                // "None of the numbers have leading zeroes"
                if (from.startsWith('0') || to.startsWith('0')) {
                    throw RuntimeException("Invalid range values $from - $to")
                }

                from.toBigInteger() to to.toBigInteger()
            }
    }
}