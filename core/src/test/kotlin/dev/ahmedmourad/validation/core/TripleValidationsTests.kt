package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.*
import org.junit.Test

class TripleValidationsTests {

    @Test
    fun first_meansTheGivenValidationsMatchTheFirstElementOfTheTriple() {
        validator<Triple<Int, Unit, Unit>> {
            first {
                max(3)
            }
        }.allMatch(
            Triple(-1, Unit, Unit),
            Triple(1, Unit, Unit),
            Triple(2, Unit, Unit),
            Triple(3, Unit, Unit)
        ).allFail(
            Triple(4, Unit, Unit),
            Triple(5, Unit, Unit),
            Triple(6, Unit, Unit)
        )
    }

    @Test
    fun second_meansTheGivenValidationsMatchTheSecondElementOfTheTriple() {
        validator<Triple<Unit, Int, Unit>> {
            second {
                max(3)
            }
        }.allMatch(
            Triple(Unit, -1, Unit),
            Triple(Unit, 1, Unit),
            Triple(Unit, 2, Unit),
            Triple(Unit, 3, Unit)
        ).allFail(
            Triple(Unit, 4, Unit),
            Triple(Unit, 5, Unit),
            Triple(Unit, 6, Unit)
        )
    }

    @Test
    fun third_meansTheGivenValidationsMatchTheThirdElementOfTheTriple() {
        validator<Triple<Unit, Unit, Int>> {
            third {
                max(3)
            }
        }.allMatch(
            Triple(Unit, Unit, -1),
            Triple(Unit, Unit, 1),
            Triple(Unit, Unit, 2),
            Triple(Unit, Unit, 3)
        ).allFail(
            Triple(Unit, Unit, 4),
            Triple(Unit, Unit, 5),
            Triple(Unit, Unit, 6)
        )
    }
}
