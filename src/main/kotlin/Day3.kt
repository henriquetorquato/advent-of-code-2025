import kotlin.time.measureTime
import java.math.BigInteger

class Day3 {
    fun solve(input: List<String>): Pair<Int, BigInteger> {
        val totalPart1 = input.sumOf { solveBankPart1(it) }

        var totalPart2 = BigInteger.ZERO
        val duration = measureTime {
            totalPart2 = input.map { solveBankPart2(it) }.reduce { a, b -> a.add(b) }
        }

        println("Part 2 took: $duration")

        return Pair(totalPart1, totalPart2)
    }

    fun solveBankPart2(bank: String): BigInteger {
        var finalValue = StringBuilder()
        var startSearchSpace = 0

        for (endWindow in 12 downTo 1) {
            val endSearchSpace = bank.length - endWindow
            val (idx, value) = highestBatteryInBetween(bank, startSearchSpace, endSearchSpace)

            startSearchSpace = idx + 1
            finalValue = finalValue.append(value.toString())
        }

        return finalValue.toString().toBigInteger()
    }

    fun highestBatteryInBetween(bank: String, start: Int, end: Int): Pair<Int, Int> {
        var highestValue = 0
        var highestIdx = 0

        for ((idx, battery) in bank.withIndex()) {
            if (idx !in start..end) {
                continue
            }

            val value = battery.digitToIntOrNull()
            if (value == null || value !in 1..9) {
                throw RuntimeException("Invalid battery value: $value")
            }

            if (value > highestValue) {
                highestValue = value
                highestIdx = idx
            }
        }

        return Pair(highestIdx, highestValue)
    }

    fun solveBankPart1(bank: String): Int {
        val (firstIdx, firstValue) = highestBatteryIn(bank.dropLast(1)) // The last character will never be the first number
        val (_, secondValue) = highestBatteryIn(bank.substring(firstIdx + 1, bank.length))
        return (firstValue.toString() + secondValue.toString()).toInt()
    }

    private fun highestBatteryIn(bank: String): Pair<Int, Int> {
        var highest = 0
        var highestIdx = 0

        for ((index, battery) in bank.withIndex()) {
            val value = battery.digitToIntOrNull()

            if (value == null || !(1..9).contains(value)) {
                throw RuntimeException("Invalid battery value: $value")
            }

            if (value > highest) {
                highest = value
                highestIdx = index
            }
        }

        return Pair(highestIdx, highest)
    }
}