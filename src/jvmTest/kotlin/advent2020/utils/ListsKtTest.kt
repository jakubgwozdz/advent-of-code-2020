package advent2020.utils

import kotlin.test.Test
import kotlin.test.expect

internal class ListsKtTest {
    @Test
    fun atLeast2a_3() {
        expect(false) {
            listOf("a", "b", "c").atLeast(3) { it > "a" }
        }
    }

    @Test
    fun atLeast2_3() {
        expect(false) {
            listOf("b", "c").atLeast(3) { it > "a" }
        }
    }

    @Test
    fun atLeast2_2() {
        expect(true) {
            listOf("b", "c").atLeast(2) { it > "a" }
        }
    }

    @Test
    fun atLeast2_0() {
        expect(true) {
            listOf("b", "c").atLeast(0) { it > "a" }
        }
    }

    @Test
    fun atLeast0_2() {
        expect(false) {
            emptyList<String>().atLeast(2) { it > "a" }
        }
    }

    @Test
    fun atLeast0_0() {
        expect(true) {
            emptyList<String>().atLeast(0) { it > "a" }
        }
    }

}
