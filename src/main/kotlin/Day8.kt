import kotlin.math.pow
import kotlin.math.sqrt

class Day8 {

    fun solve(diagram: String, connections: Int): Int {
        val junctionBoxes = diagram
            .lines()
            .map { JunctionBox.parseOrThrow(it) }
            .toTypedArray()

        // junction box id -> circuit id
        val circuits = junctionBoxes
            .mapIndexed { i, _ -> i to i } // each box starts on its own circuit
            .toMap()
            .toMutableMap()

        var totalCircuits = circuits.size

        fun addBoxToCircuit(junctionBoxId: Int, circuitId: Int) {
            circuits[junctionBoxId] = circuitId
            totalCircuits -= 1
        }

        for (i in 0..<connections) {
            for ((boxId, junctionBox) in junctionBoxes.withIndex()) {
                var closestCircuitId: Int? = null
                var closestCircuitDistance = 0f

                for (circuitId in 0..<totalCircuits) {
                    val circuitValuesKeys = circuits
                        .filter { it.value == circuitId }
                        .keys

                    if (boxId in circuitValuesKeys) {
                        continue
                    }

                    val circuit = circuitValuesKeys
                        .map { junctionBoxes[it] }

                    val distance = junctionBox.circuitDistance(circuit).min()
                    if (closestCircuitId == null || distance < closestCircuitDistance) {
                        closestCircuitId = circuitId
                        closestCircuitDistance = distance
                    }
                }

                if (closestCircuitId != null) {
                    addBoxToCircuit(boxId, closestCircuitId)
                }
            }

            val a = 0
        }

        return 0
    }

    // Keeping this since I feel like it might solve a possible part 2
    fun maybeSolvePart2(diagram: String, connections: Int): Int {
        val junctionBoxes = diagram
            .lines()
            .map { JunctionBox.parseOrThrow(it) }

        val circuits = junctionBoxes
            .mapIndexed { idx, junctionBox -> idx to listOf(junctionBox) }
            .toMap()
            .toMutableMap()

        fun mergeCircuits(a: Int, b: Int) {
            if (!circuits.containsKey(a) || !circuits.containsKey(b)) {
                return
            }

            circuits[a] = circuits[a]!! + circuits[b]!!
            circuits.remove(b)
        }

        for (i in 0..<connections) {
            var closestCircuitA: Int? = null
            var closestCircuitB: Int? = null
            var closestCircuitDistance = 0f

            for ((idxA, circuitA) in circuits) {
                for ((idxB, circuitB) in circuits) {
                    if (idxA == idxB) {
                        continue
                    }

                    for (box in circuitA) {
                        val distance = box.circuitDistance(circuitB).min()
                        if (closestCircuitA == null || distance < closestCircuitDistance) {
                            closestCircuitA = idxA
                            closestCircuitB = idxB
                            closestCircuitDistance = distance
                        }
                    }
                }
            }

            if (closestCircuitA != null && closestCircuitB != null) {
                mergeCircuits(closestCircuitA, closestCircuitB)
            }
        }

        return 0
    }

    private data class JunctionBox(
        val x: Float,
        val y: Float,
        val z: Float,
    ) {
        companion object {
            fun parseOrThrow(input: String): JunctionBox {
                val values = input
                    .split(',', limit = 3)
                    .map { it.toFloatOrNull() ?: throw RuntimeException("Invalid position value $it") }

                return JunctionBox(
                    x = values[0],
                    y = values[1],
                    z = values[2],
                )
            }
        }

        fun distance(other: JunctionBox): Float {
            val sum = listOf(
                (other.x - x).pow(2),
                (other.y - y).pow(2),
                (other.z - z).pow(2),
            ).sum()

            return sqrt(sum)
        }

        fun circuitDistance(circuit: List<JunctionBox>): List<Float>
            = circuit.map { distance(it) }
    }
}