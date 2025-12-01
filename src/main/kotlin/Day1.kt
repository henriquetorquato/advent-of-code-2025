class Day1 {
    companion object {
        fun solve(input: List<String>): Result {
            val dial = Dial()
            val finalResult = Result()

            for (rotation in input) {
                val rotationResult = dial.apply(rotation)
                finalResult.merge(rotationResult)
            }

            return finalResult
        }
    }
}

private class Dial {
    var pointer = 50

    fun apply(rotation: String): Result {
        val (direction, amount) = parseOrThrow(rotation)

        var passByZero = 0
        var atZero = 0

        for (i in 0..<amount) {
            var nextPointer = when (direction) {
                Direction.Left -> {
                    pointer - 1
                }
                Direction.Right -> {
                    pointer + 1
                }
            }

            // Check for boundaries
            nextPointer = when (nextPointer) {
                // Value when overflowing counter clock-wise from 0 will be -1
                -1 -> 99
                // Value when overflowing clock-wise from 99 will be 100
                100 -> 0
                else -> nextPointer
            }

            pointer = nextPointer

            if (pointer == 0) {
                passByZero += 1
            }
        }

        if (pointer == 0) {
            atZero += 1
        }

        return Result(atZero, passByZero)
    }

    private fun parseOrThrow(rotation: String): Pair<Direction, Int> {
        val number = rotation.substring(1..< rotation.length).toInt()
        val direction = when (rotation.toCharArray().first()) {
            'L' -> Direction.Left
            'R' -> Direction.Right
            else -> throw RuntimeException("Unable to parse rotation $rotation")
        }

        return Pair(direction, number)
    }

    private enum class Direction {
        Left,
        Right,
    }
}

data class Result(
    var totalAtZero: Int = 0,
    var totalPassByZero: Int = 0,
) {
    fun merge(result: Result) {
        totalAtZero += result.totalAtZero
        totalPassByZero += result.totalPassByZero
    }
}
