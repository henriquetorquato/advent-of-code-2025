import java.math.BigInteger

class Day6 {
    fun solve(worksheet: String): Pair<BigInteger, BigInteger> {
        val totalPart1 = solveOperations(parsePart1(worksheet))
        val totalPart2 = solveOperations(parsePart2(worksheet))
        return Pair(totalPart1, totalPart2)
    }

    fun solveOperations(operations: List<Pair<Char, List<BigInteger>>>): BigInteger
        = operations.map { (operation, numbers) ->
            when (operation) {
                '*' -> numbers.reduce { a, b -> a.multiply(b) }
                '+' -> numbers.reduce { a, b -> a.add(b) }
                else -> BigInteger.ZERO
            }
        }
        .reduce { a, b -> a.add(b) }

    fun parsePart1(worksheet: String): List<Pair<Char, List<BigInteger>>> {
        val lines = worksheet.split('\n')
        val operations = parseLine(lines.last())

        val numbers = Array(operations.size) { mutableListOf<BigInteger>() }

        for (line in lines.dropLast(1)) {
            for ((idx, numberStr) in parseLine(line).withIndex()) {
                val number = numberStr.toBigInteger()
                numbers[idx].add(number)
            }
        }

        val result = Array(operations.size) { Pair('+', listOf<BigInteger>()) }
        for ((idx, operation) in operations.withIndex()) {
            val op = operation.toCharArray().first()
            if (op != '+' && op != '*') {
                throw RuntimeException("Invalid operation character $op")
            }

            result[idx] = op to numbers[idx]
        }

        return result.toList()
    }

    fun parsePart2(worksheet: String): List<Pair<Char, List<BigInteger>>> {
        val lines = worksheet.split('\n')
        val operations = parseLine(lines.last())
            .map { it.toCharArray().first() }

        var numberIdx = 0
        val numbers = Array(operations.size) { mutableListOf<BigInteger>() }

        for (idx in lines.first().length-1 downTo 0) {
            val valueBuilder = StringBuilder()

            for (line in lines.dropLast(1)) {
                valueBuilder.append(line[idx])
            }

            val value = valueBuilder.trim().toString()
            if (value.isBlank()) {
                numberIdx += 1
                continue
            }

            numbers[numberIdx].add(value.toBigInteger())
        }

        return operations.reversed() zip numbers.toList()
    }

    fun parseLine(line: String): List<String>
        = line.split(" ").filter { it.isNotBlank() }
}