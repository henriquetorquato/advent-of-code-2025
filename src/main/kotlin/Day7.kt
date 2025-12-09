class Day7 {

    fun solve(diagram: String): Pair<Int, Int>
        = Pair(totalPart1(diagram), totalPart2(diagram))

    fun totalPart1(diagram: String): Int {
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

    fun totalPart2(diagram: String): Int {
        fun continueAt(diagram: String, idx: Int): List<String> {
            val levels = diagram
                .lines()
                .toTypedArray()

            if (idx == 0) {
                return listOf(diagram)
            } else if (idx >= levels.size) {
                return listOf()
            }

            val previous = levels[idx - 1].toCharArray()
            val current = levels[idx].toCharArray()

            // if is first line
            if (previous.contains('S')) {
                val start = previous.indexOf('S')
                current[start] = '|'

                levels[idx] = current.concatToString()

                return listOf(
                    levels.joinToString(separator = "\n")
                )
            }

            // if it has splitters
            if (current.contains('^')) {
                var dimensions = listOf<String>()
                for ((charIdx, char) in current.withIndex()) {

                }
            }
        }


    }

    private data class Node(
        val from: Node?,
        val position: Position
    )

    private data class Position(
        val x: Int,
        val y: Int
    )

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