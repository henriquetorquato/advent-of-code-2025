import java.math.BigInteger

class Day11 {

    fun solvePart2(diagram: String): BigInteger {
        val serverMap = parseOrThrow(diagram)

        val inDegreeMap = run {
            val result = mutableMapOf<String, Int>()
            serverMap.forEach { (source, paths) ->
                result.putIfAbsent(source, 0)
                paths.forEach {
                    result[it] = result.getOrDefault(it, 0) + 1
                }
            }
            return@run result
        }

        val sortedList = run {
            val rootNodes = inDegreeMap
                .filter { it.value == 0 }
                .map { it.key }

            val queue = ArrayDeque(rootNodes)
            val result = mutableListOf<String>()

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                result.add(current)

                serverMap[current]?.forEach {
                    inDegreeMap[it] = inDegreeMap[it]!! - 1
                    if (inDegreeMap[it] == 0) {
                        queue.addLast(it)
                    }
                }
            }

            return@run result
        }

        fun count(from: String, to: String): BigInteger {
            val idxFrom = sortedList.indexOf(from)

            val occurrences = mutableMapOf<String, BigInteger>()
            for (server in sortedList.subList(idxFrom, sortedList.size-1)) {
                if (server == from) {
                    occurrences[server] = BigInteger.ONE
                }

                for (neighbour in serverMap[server]!!) {
                    val neighbourOccurrences = occurrences.getOrDefault(neighbour, BigInteger.ZERO)
                    val serverOccurrences = occurrences.getOrDefault(server, BigInteger.ZERO)
                    occurrences[neighbour] = neighbourOccurrences.add(serverOccurrences)
                }
            }

            return occurrences[to] ?: BigInteger.ZERO
        }

        return count("svr", "fft") * count("fft", "dac") * count("dac", "out")
    }

    fun solvePart1(diagram: String): Int {
        val serverMap = parseOrThrow(diagram)

        fun solve(paths: List<String>): Int {
            var total = 0
            for (path in paths) {
                if (path == "out") {
                    return 1
                }

                val nextPaths = serverMap[path] ?: emptyList()
                total += solve(nextPaths)
            }

            return total
        }

        val entry = serverMap["you"] ?: emptyList()
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
