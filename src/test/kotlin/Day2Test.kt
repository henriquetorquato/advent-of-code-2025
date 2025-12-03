import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class Day2Test : ShouldSpec({
    context("isIDValidPart1") {
        listOf(
            "11",
            "22",
            "99",
            "1010",
            "1188511885",
            "222222",
            "446446",
            "38593859"
        ).forEach { id ->
            should("isIDValidPart1($id) should be false") {
                Day2().isIDValidPart1(id.toBigInteger()) shouldBe false
            }
        }

        listOf(
            1698522..1698528,
            565653..565659,
            824824821..824824827,
            2121212118..2121212124,
        ).forEach { range -> range.forEach { intId ->
            val id = intId.toBigInteger()
            should("isIDValidPart1($id) should be true") {
                Day2().isIDValidPart1(id) shouldBe true
            }
        }}
    }
    context("isIDValidPart2") {
        listOf(
            "11",
            "99",
            "111",
            "999",
            "1010",
            "1188511885",
            "222222",
            "446446",
            "38593859",
            "565656",
            "824824824",
            "2121212121",
        ).forEach { id ->
            should("isIDValidPart2($id) should be false") {
                Day2().isIDValidPart2(id.toBigInteger()) shouldBe false
            }
        }

        listOf(
            1698522..1698528,
        ).forEach { range -> range.forEach { intId ->
            val id = intId.toBigInteger()
            should("isIDValidPart2($id) should be true") {
                Day2().isIDValidPart2(id) shouldBe true
            }
        }}
    }
    context("with given input") {
        should("return test value") {
            val input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
                "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
                "824824821-824824827,2121212118-2121212124"

            val result = Day2().solve(input)
            result.first shouldBe 1227775554.toBigInteger()
            result.second shouldBe 4174379265.toBigInteger()
        }
        should("return final value") {
            val input = "851786270-851907437,27-47,577-1044,1184-1872,28214317-28368250,47766-78575,17432-28112," +
                    "2341-4099,28969-45843,5800356-5971672,6461919174-6461988558,653055-686893,76-117," +
                    "2626223278-2626301305,54503501-54572133,990997-1015607,710615-802603,829001-953096," +
                    "529504-621892,8645-12202,3273269-3402555,446265-471330,232-392,179532-201093,233310-439308," +
                    "95134183-95359858,3232278502-3232401602,25116215-25199250,5489-8293,96654-135484,2-17"

            val result = Day2().solve(input)
            println("Result part 1: ${result.first}")
            println("Result part 2: ${result.second}")

            result.first shouldBe 15873079081.toBigInteger()
            result.second shouldBe 22617871034.toBigInteger()
        }
    }
})