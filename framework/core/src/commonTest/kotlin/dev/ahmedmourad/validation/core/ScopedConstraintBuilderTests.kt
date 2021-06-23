package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.*
import kotlin.test.Test

@InternalValidationApi
class ScopedConstraintBuilderTests {

    private data class Model(val n: Int?)

    @Test
    fun validation_addsDirectValidationToThisConstraint() {

        val constraint = ScopedConstraintBuilder<Unit>().apply {
            validation { true }
        }

        assertTrue(constraint.validateAll(Unit))

        val constraint1 = ScopedConstraintBuilder<Unit>().apply {
            validation { false }
        }

        assertFalse(constraint1.validateAll(Unit))
    }

    @Test
    fun on_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ScopedConstraintBuilder<String>().apply {
            on(String::length) {
                max(5)
            }
        }

        assertTrue(constraint.validateAll("12345"))
        assertFalse(constraint.validateAll("123456"))
    }

    @Test
    fun on1_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ScopedConstraintBuilder<String>().apply {
            on({ subject.length }) {
                max(5)
            }
        }

        assertTrue(constraint.validateAll("12345"))
        assertFalse(constraint.validateAll("123456"))
    }

    @Test
    fun on2_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ScopedConstraintBuilder<String>().apply {
            on("1234"::length) {
                max(5)
            }
        }

        assertTrue(constraint.validateAll("12345"))
        assertTrue(constraint.validateAll("123456"))
    }

    @Test
    fun ifExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItSucceeds() {

        val constraint = ScopedConstraintBuilder<Model>().apply {
            on(Model::n) ifExists {
                max(5)
            }
        }

        assertTrue(constraint.validateAll(Model(null)))
        assertTrue(constraint.validateAll(Model(3)))
        assertFalse(constraint.validateAll(Model(6)))
    }

    @Test
    fun mustExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItFails() {

        val constraint = ScopedConstraintBuilder<Model>().apply {
            on(Model::n) mustExist {
                max(5)
            }
        }

        assertFalse(constraint.validateAll(Model(null)))
        assertTrue(constraint.validateAll(Model(3)))
        assertFalse(constraint.validateAll(Model(6)))
    }

    @Test
    fun validateAll_returnsTrueIfTheItemMatchesAllTheValidations() {

        val constraint = ScopedConstraintBuilder<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(constraint.validateAll(-2))
        assertTrue(constraint.validateAll(0))
        assertTrue(constraint.validateAll(2))
        assertFalse(constraint.validateAll(1))
        assertFalse(constraint.validateAll(3))
        assertFalse(constraint.validateAll(4))
        assertFalse(constraint.validateAll(5))
        assertFalse(constraint.validateAll(6))
    }

    @Test
    fun validateAny_returnsTrueIfTheItemMatchesAnyOfTheValidations() {

        val constraint = ScopedConstraintBuilder<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(constraint.validateAny(-2))
        assertTrue(constraint.validateAny(-1))
        assertTrue(constraint.validateAny(0))
        assertTrue(constraint.validateAny(1))
        assertTrue(constraint.validateAny(2))
        assertTrue(constraint.validateAny(3))
        assertTrue(constraint.validateAny(4))
        assertTrue(constraint.validateAny(6))
        assertFalse(constraint.validateAny(5))
        assertFalse(constraint.validateAny(7))
        assertFalse(constraint.validateAny(9))
    }

    @Test
    fun validateNone_returnsTrueIfTheItemMatchesNoneOfTheValidations() {

        val constraint = ScopedConstraintBuilder<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(constraint.validateNone(5))
        assertTrue(constraint.validateNone(7))
        assertTrue(constraint.validateNone(9))
        assertFalse(constraint.validateNone(-2))
        assertFalse(constraint.validateNone(-1))
        assertFalse(constraint.validateNone(0))
        assertFalse(constraint.validateNone(1))
        assertFalse(constraint.validateNone(2))
        assertFalse(constraint.validateNone(3))
        assertFalse(constraint.validateNone(4))
        assertFalse(constraint.validateNone(6))
    }
}
