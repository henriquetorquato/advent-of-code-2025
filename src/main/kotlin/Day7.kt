import kotlin.time.measureTime
import java.math.BigInteger

class Day7 {

    fun solve(diagram: String): Pair<Int, BigInteger> {
        val totalPart1 = totalPart1(diagram)

        var totalPart2: BigInteger
        val timePart2 = measureTime {
            totalPart2 = totalPart2(diagram)
        }

        println("Part 2 finished with: $timePart2")
        return Pair(totalPart1, totalPart2)
    }

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

    fun totalPart2(diagram: String): BigInteger {
        val levels = diagram
            .lines()
            .toTypedArray()
            .map { it.toCharArray() }

        val start = levels
            .first()
            .indexOf('S')

        val root = Node(
            path = listOf(start),
            depth = 1,
        )

        var iterations = BigInteger.ZERO
        var total = BigInteger.ZERO

        fun next(node: Node) {
            if (iterations.remainder(BigInteger.valueOf(5000000)) == BigInteger.ZERO) {
                val previous = node.previous()
                println("[$iterations] Total: $total. Node: Depth: ${node.depth}, Incoming beam: $previous.")
            }
            iterations = iterations.add(BigInteger.ONE)

            if (node.depth == levels.size) {
                total = total.add(BigInteger.ONE)
                return
            }

            val incomingBeam = node.previous()
            val splitters = levels[node.depth]
                .mapIndexed { idx, char -> if (char == '^') idx else null }
                .filterNotNull()

            val path = node.path.toList()
            for (splitter in splitters) {
                if (incomingBeam == splitter) {
                    val leftNode = Node(
                        path = path + (splitter - 1),
                        depth = node.nextDepth(),
                    )

                    next(leftNode)

                    val rightNode = Node(
                        path = path + (splitter + 1),
                        depth = node.nextDepth(),
                    )

                    next(rightNode)
                }
            }

            if (incomingBeam !in splitters) {
                val nextNode = Node(
                    path = path + incomingBeam,
                    depth = node.nextDepth(),
                )

                next(nextNode)
            }
        }
        next(root)

        return total
    }

    private data class Node(
        val path: List<Int>,
        val depth: Int,
    ) {
        fun previous(): Int = path[depth - 1]
        fun nextDepth(): Int = depth + 1
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