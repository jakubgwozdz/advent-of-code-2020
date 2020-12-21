package advent2020.day21

fun part1(input: String): String {
    val foods = foods(input)
    val ingredients = foods.map { it.first }.flatten()

    val unsafe = matches(foods).values.reduce(Set<String>::plus)
    val safe = ingredients.filterNot { it in unsafe }.toSet()

    val result = ingredients.count { it in safe }

    return result.toString()
}

fun part2(input: String): String {
    val foods = foods(input)
    val matches = matches(foods)

    var again = true
    while (again) {
        again = false
        matches
            .mapNotNull { (allergen, ingredients) -> ingredients.singleOrNull()?.let { allergen to it } }
            .forEach { (allergen, ingredient) ->
                matches.forEach { (a2, i2) ->
                    if (a2 != allergen && ingredient in i2) i2 -= ingredient.also {
                        again = true
                    }
                }
            }
    }

    return matches.entries.sortedBy { it.key }.joinToString(",") { it.value.single() }
}

private fun matches(foods: List<Pair<List<String>, List<String>>>) = foods
    .map { (ingredients, allergens) ->
        allergens.map { it to ingredients.toSet() }.toMap()
    }
    .reduce { acc, next ->
        val keys = acc.keys + next.keys
        keys.associateWith {
            val s1 = acc[it]
            val s2 = next[it]
            when {
                s1 == null -> s2!!
                s2 == null -> s1
                else -> s1.intersect(s2)
            }
        }
    }
    .mapValues { it.value.toMutableSet() }

val regex by lazy { """((\w+ )+)\(contains ((\w+(, )?)+)\)""".toRegex() }

private fun foods(input: String): List<Pair<List<String>, List<String>>> =
    input.trim().lines()
        .map { regex.matchEntire(it)?.destructured ?: error("`$it` doesn't match") }
        .map { (gr1, _, gr3) ->
            val ingredients = gr1.split(" ").filter { it.isNotBlank() }
            val allergens = gr3.split(", ").filter { it.isNotBlank() }
            ingredients to allergens
        }


