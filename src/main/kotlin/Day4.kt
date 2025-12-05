class Day4 {
    companion object {
        const val LEFT_BOUND = 0
        const val TOP_BOUND = 0
    }

    fun solve(diagram: String): Int {
        val grid = parseOrThrow(diagram)
        var accessibleRolls = 0

        val debug = Array(grid.size) { Array(grid.first().size) { '.' } }

        for (y in 0..<grid.size) {
            val bottomBound = grid.size - 1
            val row = grid[y]

            for (x in 0..<row.size) {
                val hasRoll = grid[y][x]
                if (!hasRoll) {
                    continue
                }

                val rightBound = row.size - 1
                var adjacent = 0

                // Top-left
                if (y > TOP_BOUND && x > LEFT_BOUND && grid[y-1][x-1]) {
                    adjacent += 1
                }

                // Top
                if (y > TOP_BOUND && grid[y-1][x]) {
                    adjacent += 1
                }

                // Top-right
                if (y > TOP_BOUND && x < rightBound && grid[y-1][x+1]) {
                    adjacent += 1
                }

                // Left
                if (x > LEFT_BOUND && grid[y][x-1]) {
                    adjacent += 1
                }

                // Right
                if (x < rightBound && grid[y][x+1]) {
                    adjacent += 1
                }

                // Bottom-left
                if (y < bottomBound && x > LEFT_BOUND && grid[y+1][x-1]) {
                    adjacent += 1
                }

                // Bottom
                if (y < bottomBound && grid[y+1][x]) {
                    adjacent += 1
                }

                // Bottom-right
                if (y < bottomBound && x < rightBound && grid[y+1][x+1]) {
                    adjacent += 1
                }

                if (adjacent < 4) {
                    accessibleRolls += 1
                    debug[y][x] = 'x'
                } else {
                    debug[y][x] = when (grid[y][x]) {
                        true -> '@'
                        false -> '.'
                    }
                }
            }
        }

        return accessibleRolls
    }

    fun parseOrThrow(diagram: String): List<List<Boolean>> {
        return diagram
            .split('\n')
            .map { row -> row.toCharArray().map { char ->
                when (char) {
                    '.' -> false
                    '@' -> true
                    else -> throw RuntimeException("Invalid diagram character $char")
                }
            }}
    }
}