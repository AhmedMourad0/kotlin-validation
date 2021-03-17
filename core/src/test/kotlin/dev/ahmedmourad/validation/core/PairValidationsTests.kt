package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.*
import org.junit.Test

class PairValidationsTests {

    @Test
    fun first_meansTheGivenValidationsMatchTheFirstElementOfThePair() {
        validator<Pair<Int, Unit>> {
            first {
                max(3)
            }
        }.allMatch(
            -1 to Unit,
            1 to Unit,
            2 to Unit,
            3 to Unit
        ).allFail(
            4 to Unit,
            5 to Unit,
            6 to Unit
        )
    }

    @Test
    fun second_meansTheGivenValidationsMatchTheSecondElementOfThePair() {
        validator<Pair<Unit, Int>> {
            second {
                max(3)
            }
        }.allMatch(
            Unit to -1,
            Unit to 1,
            Unit to 2,
            Unit to 3
        ).allFail(
            Unit to 4,
            Unit to 5,
            Unit to 6
        )
    }
}
