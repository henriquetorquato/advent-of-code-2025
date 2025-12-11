class Day7 {

    fun solve(diagram: String): Pair<Int, Long>
        = Pair(solvePart1(diagram), solvePart2(diagram))

    fun solvePart1(diagram: String): Int {
        val solved = solveDiagram(diagram)
            .lines()
            .toTypedArray()

        var total = 0

        for (level in 1..<solved.size) {
            val previous = solved[level-1].toCharArray()
            val current = solved[level].toCharArray()

            for ((idx, char) in current.withIndex()) {
                if (char == '^' && previous[idx] == '|') {
                    total += 1
                }
            }
        }

        return total
    }

    fun solvePart2(diagram: String): Long {
        val levels = diagram
            .lines()
            .toTypedArray()
            .map { it.toCharArray() }

        val splitterMap = levels
            .mapIndexed { levelIdx, level ->
                if (!level.contains('^')) {
                    return@mapIndexed null
                }

                val splitters = level
                    .mapIndexed { idx, char -> if (char == '^') idx else null }
                    .filterNotNull()

                return@mapIndexed levelIdx to splitters
            }
            .filterNotNull()
            .toMap()

        val start = levels
            .first()
            .indexOf('S')

        val cache: MutableMap<Pair<Int, Int>, Long> = HashMap()

        fun next(incomingBeam: Int, level: Int): Long {
            if (level == levels.size) {
                return 1
            }

            val cacheKey = incomingBeam to level
            val cacheValue = cache[cacheKey]
            if (cacheValue != null) {
                return cacheValue
            }

            var total: Long = 0
            val splitters = splitterMap[level] ?: listOf()

            for (splitter in splitters) {
                if (incomingBeam == splitter) {
                    total += next(splitter - 1, level + 1)
                    total += next(splitter + 1, level + 1)
                }
            }

            if (incomingBeam !in splitters) {
                total += next(incomingBeam, level + 1)
            }

            cache[cacheKey] = total
            return total
        }
        return next(start, 1)
    }

    fun solveDiagram(diagram: String): String {
        val levels = diagram
            .lines()
            .toTypedArray()

        for (idx in 1..<levels.size) {
            val previous = levels[idx-1].toCharArray()
            val current = levels[idx].toCharArray()

            for ((idx, character) in current.withIndex()) {
                if (character == '^') {
                    current[idx-1] = '|'
                    current[idx+1] = '|'
                } else if (previous[idx] == '|' || previous[idx] == 'S') {
                    current[idx] = '|'
                }
            }

            levels[idx] = current.concatToString()
        }

        return levels.joinToString(separator = "\n")
    }
}