class Day11 {
    companion object {
        const val ENTRY = "you"
        const val EXIT = "out"
    }

    fun solve(diagram: String): Int {
        val serverMap = parseOrThrow(diagram)

        fun solve(paths: List<String>): Int {
            var total = 0
            for (path in paths) {
                if (path == EXIT) {
                    return 1
                }

                val nextPaths = serverMap[path] ?: emptyList()
                total += solve(nextPaths)
            }

            return total
        }

        val entry = serverMap[ENTRY] ?: emptyList()
        return solve(entry)
    }

    private fun parseOrThrow(diagram: String): Map<String, List<String>> = diagram
        .lines()
        .map { it.split(" ") }
        .associate { parts ->
            val key = parts.first().trimEnd(':')
            key to parts.drop(1)
        }
}