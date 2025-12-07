import java.math.BigInteger

class Day5 {
    fun solve(
        freshIngredientsStr: List<String>,
        availableIngredientsStr: List<String>
    ): Pair<Int, BigInteger> {
        val sortedFreshIngredients = parseFreshIngredients(freshIngredientsStr)
            .sortedBy { it.start }

        var freshIngredients = listOf<ClosedRange<BigInteger>>()
        var current = sortedFreshIngredients.first()

        for (next in sortedFreshIngredients.drop(1)) {
            if (next.start <= current.endInclusive.plus(BigInteger.ONE)) {
                current = current.start..current.endInclusive.max(next.endInclusive)
            } else {
                freshIngredients = freshIngredients + current
                current = next
            }
        }

        freshIngredients = freshIngredients + current

        val availableIngredients = availableIngredientsStr.map { it.toBigInteger() }

        var totalPart1 = 0
        for (ingredient in availableIngredients) {
            if (sortedFreshIngredients.any { it.contains(ingredient) }) {
                totalPart1 += 1
            }
        }

        val totalPart2 = freshIngredients
            .map { it.endInclusive.subtract(it.start).add(BigInteger.ONE) }
            .reduce { a, b -> a.add(b) }

        return Pair(totalPart1, totalPart2)
    }

    fun parseFreshIngredients(freshIngredients: List<String>): List<ClosedRange<BigInteger>> {
        return freshIngredients
            .map { it.split('-', limit = 2) }
            .map { pieces -> pieces[0].toBigInteger().rangeTo(pieces[1].toBigInteger()) }
    }
}
