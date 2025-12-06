class Day4 {
    companion object {
        const val LEFT_BOUND = 0
        const val TOP_BOUND = 0
    }

    fun solve(diagram: String): Pair<Int, Int> {
        var grid = parseOrThrow(diagram)
        val (nextGrid, totalPart1) = removeRolls(grid)

        var totalPart2 = totalPart1
        grid = nextGrid

        do {
            val (nextGrid, total) = removeRolls(grid)
            totalPart2 += total
            grid = nextGrid
        } while (total > 0)

        return Pair(totalPart1, totalPart2)
    }

    fun removeRolls(grid: Array<Array<Boolean>>): Pair<Array<Array<Boolean>>, Int> {
        var accessibleRolls = 0
        val result = Array(grid.size) { grid[it].copyOf() }

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
                    result[y][x] = false
                }
            }
        }

        return Pair(result, accessibleRolls)
    }

    fun parseOrThrow(diagram: String): Array<Array<Boolean>> {
        return diagram
            .split('\n')
            .map { row -> row.toCharArray().map { char ->
                    when (char) {
                        '.' -> false
                        '@' -> true
                        else -> throw RuntimeException("Invalid diagram character $char")
                    }
                }.toTypedArray()
            }.toTypedArray()
    }
}
