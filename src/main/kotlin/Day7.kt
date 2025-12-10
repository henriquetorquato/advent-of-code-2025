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
        var total = 0

        val levels = diagram
            .lines()
            .toTypedArray()
            .map { it.toCharArray() }

        val start = levels
            .first()
            .indexOf('S')

        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(Pair(start, 1))

        while (queue.isNotEmpty()) {
            val (incomingBeam, currentLevel) = queue.removeFirst()

            if (currentLevel == levels.size) {
                total += 1
                continue
            }

            val splitters = levels[currentLevel]
                .mapIndexed { idx, char -> if (char == '^') idx else null }
                .filterNotNull()

            var outgoingBeams = setOf<Int>()

            for (splitter in splitters) {
                if (incomingBeam == splitter) {
                    outgoingBeams = outgoingBeams + (splitter - 1)
                    outgoingBeams = outgoingBeams + (splitter + 1)
                }
            }

            if (incomingBeam !in splitters) {
                outgoingBeams = outgoingBeams + incomingBeam
            }

            if (outgoingBeams.isNotEmpty()) {
                queue.addAll(outgoingBeams.map { it to currentLevel + 1 })
            }
        }

        return total
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