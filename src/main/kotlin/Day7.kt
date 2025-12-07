class Day7 {

    fun solve(diagram: String): Int {
        val solved = solveDiagram(diagram)
            .lines()
            .toTypedArray()

        var totalPart1 = 0

        for (level in 1..<solved.size) {
            val previous = solved[level-1].toCharArray()
            val current = solved[level].toCharArray()

            for ((idx, char) in current.withIndex()) {
                if (char == '^' && previous[idx] == '|') {
                    totalPart1 += 1
                }
            }
        }

        return totalPart1
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