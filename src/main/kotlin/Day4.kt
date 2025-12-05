class Day4 {

    fun solve(diagram: String): Int {
        val grid = parseOrThrow(diagram)

        for (y in 0..grid.size) {
            val row = grid[y]
            for (x in 0..row.size) {
                val hasRoll = grid[y][x]
                if (!hasRoll) {
                    continue
                }


            }
        }
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