package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.isFalse
import dev.ahmedmourad.validation.core.validations.isTrue
import kotlin.test.Test

class BooleanValidationsTests {

    @Test
    fun isTrue_meansThisBooleanEqualsTrue() {
        constraint<Boolean> {
            isTrue()
        }.allMatch(
            true
        ).allFail(
            false
        )
    }

    @Test
    fun isFalse_meansThisBooleanEqualsFalse() {
        constraint<Boolean> {
            isFalse()
        }.allMatch(
            false
        ).allFail(
            true
        )
    }
}
