import kotlin.math.pow
import kotlin.math.sqrt

class Day8 {

    fun solve(diagram: String, connections: Int): Int {
        val junctionBoxes = parseOrThrow(diagram)
        var distances = listOf<Triple<Int, Int, Float>>() // box A, box B, distance

        for ((idA, boxA) in junctionBoxes.withIndex()) {
            for (idB in (idA + 1)..<junctionBoxes.size) {
                val boxB = junctionBoxes[idB]
                val distance = boxA.distance(boxB)
                distances = distances + Triple(idA, idB, distance)
            }
        }

        distances = distances
            .sortedBy { it.third }
            .take(connections)

        // circuit id -> list(box id)
        var nextCircuitId = 0
        val circuits = HashMap<Int, List<Int>>()

        fun findCircuit(junctionBoxId: Int): Int? {
            for ((id, junctionBoxes) in circuits) {
                if (junctionBoxId !in junctionBoxes) {
                    continue
                }

                return id
            }

            return null
        }

        fun mergeCircuits(a: Int, b: Int) {
            val valuesA = circuits[a]!!
            val valuesB = circuits[b]!!

            circuits.remove(b)
            circuits[a] = valuesA + valuesB
        }

        fun addToCircuit(circuitId: Int, boxId: Int) {
            val values = circuits.getOrDefault(circuitId, listOf())
            circuits[circuitId] = values  + boxId
        }

        for ((boxA, boxB, _) in distances) {
            val circuitA = findCircuit(boxA)
            val circuitB = findCircuit(boxB)

            if (circuitA != null && circuitB != null) {
                if (circuitA == circuitB) {
                    continue
                }

                mergeCircuits(circuitA, circuitB)
            } else {
                if (circuitA != null) {
                    addToCircuit(circuitA, boxB)
                } else if (circuitB != null) {
                    addToCircuit(circuitB, boxA)
                } else {
                    val circuitId = nextCircuitId++
                    addToCircuit(circuitId, boxA)
                    addToCircuit(circuitId, boxB)
                }
            }
        }

        val totalPart1 = circuits
            .map { it.value.count() }
            .sortedDescending()
            .take(3)
            .reduce { a, b -> a * b }

        return totalPart1
    }

    private fun parseOrThrow(diagram: String): Array<JunctionBox> {
        return diagram
            .lines()
            .map { JunctionBox.parseOrThrow(it) }
            .toTypedArray()
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
    }
}