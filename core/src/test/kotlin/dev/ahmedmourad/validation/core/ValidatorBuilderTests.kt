package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.*
import org.junit.Assert.*
import org.junit.Test

@InternalValidationApi
class ValidatorBuilderTests {

    private data class Model(val n: Int?)

    @Test
    fun validation_addsDirectValidationToThisConstraint() {

        val validator = ValidatorImpl<Unit>().apply {
            validation { true }
        }

        assertTrue(validator.validateAll(Unit))

        val validator1 = ValidatorImpl<Unit>().apply {
            validation { false }
        }

        assertFalse(validator1.validateAll(Unit))
    }

    @Test
    fun on_changesTheValidatorScopeFromTheConstrainedTypeToTheGivenItem() {

        val validator = ValidatorImpl<String>().apply {
            on(String::length) {
                max(5)
            }
        }

        assertTrue(validator.validateAll("12345"))
        assertFalse(validator.validateAll("123456"))
    }

    @Test
    fun on1_changesTheValidatorScopeFromTheConstrainedTypeToTheGivenItem() {

        val validator = ValidatorImpl<String>().apply {
            on({ it.length }) {
                max(5)
            }
        }

        assertTrue(validator.validateAll("12345"))
        assertFalse(validator.validateAll("123456"))
    }

    @Test
    fun on2_changesTheValidatorScopeFromTheConstrainedTypeToTheGivenItem() {

        val validator = ValidatorImpl<String>().apply {
            on("1234"::length) {
                max(5)
            }
        }

        assertTrue(validator.validateAll("12345"))
        assertTrue(validator.validateAll("123456"))
    }

    @Test
    fun ifExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItSucceeds() {

        val validator = ValidatorImpl<Model>().apply {
            on(Model::n) ifExists {
                max(5)
            }
        }

        assertTrue(validator.validateAll(Model(null)))
        assertTrue(validator.validateAll(Model(3)))
        assertFalse(validator.validateAll(Model(6)))
    }

    @Test
    fun mustExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItFails() {

        val validator = ValidatorImpl<Model>().apply {
            on(Model::n) mustExist {
                max(5)
            }
        }

        assertFalse(validator.validateAll(Model(null)))
        assertTrue(validator.validateAll(Model(3)))
        assertFalse(validator.validateAll(Model(6)))
    }

    @Test
    fun validateAll_returnsTrueIfTheItemMatchesAllTheValidations() {

        val validator = ValidatorImpl<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(validator.validateAll(-2))
        assertTrue(validator.validateAll(0))
        assertTrue(validator.validateAll(2))
        assertFalse(validator.validateAll(1))
        assertFalse(validator.validateAll(3))
        assertFalse(validator.validateAll(4))
        assertFalse(validator.validateAll(5))
        assertFalse(validator.validateAll(6))
    }

    @Test
    fun validateAny_returnsTrueIfTheItemMatchesAnyOfTheValidations() {

        val validator = ValidatorImpl<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(validator.validateAny(-2))
        assertTrue(validator.validateAny(-1))
        assertTrue(validator.validateAny(0))
        assertTrue(validator.validateAny(1))
        assertTrue(validator.validateAny(2))
        assertTrue(validator.validateAny(3))
        assertTrue(validator.validateAny(4))
        assertTrue(validator.validateAny(6))
        assertFalse(validator.validateAny(5))
        assertFalse(validator.validateAny(7))
        assertFalse(validator.validateAny(9))
    }

    @Test
    fun validateNone_returnsTrueIfTheItemMatchesNoneOfTheValidations() {

        val validator = ValidatorImpl<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(validator.validateNone(5))
        assertTrue(validator.validateNone(7))
        assertTrue(validator.validateNone(9))
        assertFalse(validator.validateNone(-2))
        assertFalse(validator.validateNone(-1))
        assertFalse(validator.validateNone(0))
        assertFalse(validator.validateNone(1))
        assertFalse(validator.validateNone(2))
        assertFalse(validator.validateNone(3))
        assertFalse(validator.validateNone(4))
        assertFalse(validator.validateNone(6))
    }
}
