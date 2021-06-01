package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.validator
import dev.ahmedmourad.validation.core.validations.isFalse
import dev.ahmedmourad.validation.core.validations.isTrue
import org.junit.Test

class BooleanValidationsTests {

    @Test
    fun isTrue_meansThisBooleanEqualsTrue() {
        validator<Boolean> {
            isTrue()
        }.allMatch(
            true
        ).allFail(
            false
        )
    }

    @Test
    fun isFalse_meansThisBooleanEqualsFalse() {
        validator<Boolean> {
            isFalse()
        }.allMatch(
            false
        ).allFail(
            true
        )
    }
}
