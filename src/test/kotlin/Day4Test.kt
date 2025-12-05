import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class Day4Test : ShouldSpec({
    context("with given input") {
        should("return test value") {
            val input = """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
            """.trimIndent().trimStart('\n')

            val unit = Day4()

            val diagram = unit.parseOrThrow(input)
            diagram[0] shouldBe listOf(false, false, true, true, false, true, true, true, true, false)
            diagram[1] shouldBe listOf(true, true, true, false, true, false, true, false, true, true)
            diagram[2] shouldBe listOf(true, true, true, true, true, false, true, false, true, true)
            diagram[3] shouldBe listOf(true, false, true, true, true, true, false, false, true, false)
            diagram[4] shouldBe listOf(true, true, false, true, true, true, true, false, true, true)
        }
    }
})