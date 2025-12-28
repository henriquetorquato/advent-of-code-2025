import java.util.LinkedList
import java.util.Queue

class Day10 {

    fun solve(diagram: String): Int {
        val machines = diagram
            .lines()
            .map { parseOrThrow(it) }

        val solution = machines
            .sumOf { solveMachine(it) }

        return solution
    }

    fun solveMachine(machine: Machine): Int {
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
            buttons = buttons,
            joltageRequirements = joltageValues,
        )
    }

    data class Machine(
        val targetIndicator: IndicatorLights,
        val buttons: List<Button>,
        val joltageRequirements: List<Int>,
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
}
