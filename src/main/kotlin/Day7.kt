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
        val queue = ArrayDeque<Pair<String, Int>>()
        queue.add(Pair(diagram, 0))

        while (queue.isNotEmpty()) {
            val (diagram, idx) = queue.removeFirst()
            val (currentTimeline, nextTimelines) = continueAt(diagram, idx)

            if (nextTimelines.isNotEmpty()) {
                queue.addAll(nextTimelines.map { it to idx + 1 })
            } else if (currentTimeline != "") {
                queue.add(currentTimeline to idx + 1)
            } else {
                total += 1
            }
        }

        return total
    }

    fun continueAt(diagram: String, lineIdx: Int): Pair<String, List<String>> {
        val levels = diagram
            .lines()
            .toTypedArray()

        if (lineIdx == 0) {
            return diagram to listOf()
        } else if (lineIdx >= levels.size) {
            return "" to listOf()
        }

        val previous = levels[lineIdx - 1].toCharArray()
        val current = levels[lineIdx].toCharArray()

        if (previous.contains('S')) { // if is first line
            return Pair(
                diagramWithBeamAt(diagram, lineIdx, previous.indexOf('S')),
                listOf(),
            )
        }

        var dimensions = listOf<String>()

        for ((charIdx, char) in current.withIndex()) {
            if (previous[charIdx] != '|') {
                continue
            }

            if (char == '^') {
                dimensions = dimensions + diagramWithBeamAt(diagram, lineIdx, charIdx - 1)
                dimensions = dimensions + diagramWithBeamAt(diagram, lineIdx, charIdx + 1)
            } else {
                dimensions = dimensions + diagramWithBeamAt(diagram, lineIdx, charIdx)
            }
        }

        return diagram to dimensions
    }

    fun diagramWithBeamAt(diagram: String, lineIdx: Int, colIdx: Int): String {
        val lines = diagram
            .lines()
            .toTypedArray()

        val cols = lines[lineIdx].toCharArray()
        cols[colIdx] = '|'

        lines[lineIdx] = cols.concatToString()
        return lines.joinToString("\n")
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