import kotlin.math.min
import java.util.*

class Day10 {

    fun solve(diagram: String): Pair<Int, Int> {
        val machines = diagram
            .lines()
            .map { parseOrThrow(it) }

        val totalPart1 = machines
            .sumOf { solveMachinePart1(it) }

        val totalPart2 = machines
            .mapIndexed { idx, machine ->
                println("[+] Starting processing machine with id $idx")
                val result = solveMachinePart2(machine)
                println("[+] Finished processing machine with id $idx: $result")
                return@mapIndexed result
            }
            .sum()

        return Pair(totalPart1, totalPart2)
    }

    /*
    * This solution assumes that the target joltage will also the same indicator light profile based on the problem
    *  description.
    *
    * It assumes that the best solution for the target joltage is the smallest amount of presses to achieve the lights
    *  profile, followed by a series of even button presses, causing the light profile to be unchanged but the joltage
    *  value to go up. And, since the button presses are even, any value they might contribute will also be even,
    *  making it possible to shrink the search space even more by dividing the target joltage by 2.
    *
    * This solution was yoinked from:
    * https://old.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/
    * */
    fun solveMachinePart2(machine: Machine): Int {
        val cache = run {
            val cache: MutableMap<Int, MutableMap<Joltage, Int>> = HashMap()

            fun addEntry(joltage: Joltage, cost: Int) {
                val parityPattern = joltage
                    .indicatorLights()
                    .mask

                val maskEntry = cache.getOrDefault(parityPattern, HashMap())

                val joltageEntry = maskEntry[joltage]
                maskEntry[joltage] = if (joltageEntry == null) cost
                    else min(cost, joltageEntry)

                cache[parityPattern] = maskEntry
            }

            fun combinations(size: Int): Sequence<List<Button>> = sequence {
                val pool = machine.buttons

                if (size > pool.size) return@sequence
                val indices = IntArray(size) { it }

                yield(indices.map { pool[it] })

                while (true) {
                    var i = size - 1
                    while (i >= 0 && indices[i] == i + pool.size - size) {
                        i--
                    }

                    if (i < 0) break

                    indices[i]++
                    for (j in i + 1 until size) {
                        indices[j] = indices[j - 1] + 1
                    }

                    yield(indices.map { pool[it] })
                }
            }

            for (buttonsSize in 0..machine.buttons.size) {
                for (presses in combinations(buttonsSize)) {
                    val joltage = Joltage
                        .ofSize(machine.targetJoltage.size)
                        .applyAll(presses)

                    addEntry(joltage, presses.size)
                }
            }

            return@run cache.toMap()
        }

        fun calculate(state: Joltage): Int? {
            if (state.isEmpty()) {
                return 0
            }

            val patterns = cache[state.indicatorLights().mask]
                ?: return null

            var answer: Int? = null
            for ((pattern, cost) in patterns) {
                if (pattern.isUnreachable(state)) {
                    continue
                }

                val nextState = pattern
                    .until(state)
                    .half()

                val newAnswer = calculate(nextState)?.let { cost + it * 2 }
                if (newAnswer == null) {
                    continue
                }

                if (answer == null || newAnswer < answer) {
                    answer = newAnswer
                }
            }

            return answer
        }

        return calculate(machine.targetJoltage)
            ?: throw RuntimeException()
    }

    fun solveMachinePart1(machine: Machine): Int {
        val queue: Queue<Pair<Int, List<Int>>> = LinkedList()
        queue.add(0 to listOf())

        var visited = setOf(0)

        while (queue.isNotEmpty()) {
            val (state, presses) = queue.poll()

            if (state == machine.targetIndicator.mask) {
                return presses.count()
            }

            for (button in machine.buttons) {
                val nextState = state xor button.mask

                if (nextState in visited) {
                    continue
                }

                visited = visited + nextState
                queue.add(nextState to presses + button.mask)
            }
        }

        return 0
    }

    fun parseOrThrow(diagram: String): Machine {
        val parts = diagram
            .split(' ')

        val indicatorValues = parts
            .first()
            .drop(1)
            .dropLast(1)
            .map { it == '#' }
            .toBooleanArray()

        val joltageValues = parts
            .last()
            .drop(1)
            .dropLast(1)
            .split(',')
            .map { it.toInt() }
            .toList()

        val buttons = parts
            .drop(1)
            .dropLast(1)
            .map { buttonDiagram -> buttonDiagram
                    .drop(1)
                    .dropLast(1)
                    .split(',')
                    .map { it.toInt() } }
            .map { Button(it) }

        return Machine(
            targetIndicator = IndicatorLights(indicatorValues),
            targetJoltage = Joltage(joltageValues),
            buttons = buttons,
        )
    }

    data class Machine(
        val targetIndicator: IndicatorLights,
        val targetJoltage: Joltage,
        val buttons: List<Button>,
    )

    class IndicatorLights(
        val values: BooleanArray
    ) {
        val mask = run {
            var mask = 0
            for ((idx, value) in values.withIndex()) {
                if (!value) {
                    continue
                }

                mask = mask or (1 shl idx)
            }

            return@run mask
        }
    }

    data class Button(
        val values: List<Int>
    ) {
        val mask = run {
            var mask = 0
            for (idx in values) {
                mask = mask or (1 shl idx)
            }

            return@run mask
        }
    }

    data class Joltage(
        val values: List<Int>
    ) {
        companion object {
            fun ofSize(size: Int): Joltage
                = Joltage(IntArray(size).toList())
        }

        val size = values.size

        fun applyAll(buttons: List<Button>): Joltage {
            var newJoltage = this.copy()
            for (button in buttons) {
                newJoltage = newJoltage.apply(button)
            }

            return newJoltage
        }

        fun apply(button: Button): Joltage {
            val newValues = values.toTypedArray()
            for (button in button.values) {
                newValues[button] += 1
            }
            return Joltage(newValues.toList())
        }

        fun until(targetJoltage: Joltage): Joltage {
            val values = values.toTypedArray()
            for ((idx, targetValue) in targetJoltage.values.withIndex()) {
                values[idx] = targetValue - values[idx]
            }
            return Joltage(values.toList())
        }

        fun half(): Joltage {
            val values = values.toTypedArray()
            for ((idx, value) in values.withIndex()) {
                /*
                * Since we are shrinking the problem space and assuming that an even number of presses is needed to
                *  achieve the final result, when this function is called, it should only contain even numbers.
                * */
                if (value % 2 != 0) {
                    throw RuntimeException("Invalid or unreachable input")
                }

                values[idx] = value / 2
            }
            return Joltage(values.toList())
        }

        fun isUnreachable(targetJoltage: Joltage): Boolean {
            for ((idx, targetValue) in targetJoltage.values.withIndex()) {
                val currentValue = values[idx]
                if (currentValue <= targetValue && currentValue.isOdd() == targetValue.isOdd()) {
                    continue
                }

                return true
            }

            return false
        }

        fun isEmpty(): Boolean
            = values.all { it == 0 }

        fun indicatorLights(): IndicatorLights
            = IndicatorLights(values.map { it % 2 != 0 }.toBooleanArray())
    }
}
