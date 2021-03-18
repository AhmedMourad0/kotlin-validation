package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.validator
import dev.ahmedmourad.validation.core.validations.*
import org.junit.Test

class NullableValidationsTests {

    @Test
    fun ifExists_meansThatThisObjectMustMatchTheGivenValidationsIfAndOnlyIfItIsNotNull() {
        validator<Int?> {
            ifExists {
                max(3)
            }
        }.allMatch(
            null,
            -1,
            0,
            1,
            2,
            3
        ).allFail(
            4,
            5,
            6
        )
    }

    @Test
    fun mustExist_meansThatThisObjectCannotBeNullAndMustMatchTheGivenValidations() {
        validator<Int?> {
            mustExist {
                max(3)
            }
        }.allMatch(
            -1,
            0,
            1,
            2,
            3
        ).allFail(
            null,
            4,
            5,
            6
        )
    }

    @Test
    fun exists_meansThatThisObjectCannotBeNull() {
        validator<Int?> {
            exists()
        }.allMatch(
            -1,
            0,
            1,
            2,
            3
        ).allFail(
            null
        )
    }

    @Test
    fun exists_meansThatThisObjectMustBeNull() {
        validator<Int?> {
            doesNotExist()
        }.allMatch(
            null
        ).allFail(
            -1,
            0,
            1,
            2,
            3
        )
    }
}
