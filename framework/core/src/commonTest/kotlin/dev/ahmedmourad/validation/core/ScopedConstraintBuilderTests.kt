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

        assertTrue(constraint.matchesAll(Unit))

        val constraint1 = ScopedConstraintBuilder<Unit>().apply {
            validation { false }
        }

        assertFalse(constraint1.matchesAll(Unit))
    }

    @Test
    fun on_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ScopedConstraintBuilder<String>().apply {
            on(String::length) {
                max(5)
            }
        }

        assertTrue(constraint.matchesAll("12345"))
        assertFalse(constraint.matchesAll("123456"))
    }

    @Test
    fun on1_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ScopedConstraintBuilder<String>().apply {
            on({ subject.length }) {
                max(5)
            }
        }

        assertTrue(constraint.matchesAll("12345"))
        assertFalse(constraint.matchesAll("123456"))
    }

    @Test
    fun on2_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ScopedConstraintBuilder<String>().apply {
            on("1234"::length) {
                max(5)
            }
        }

        assertTrue(constraint.matchesAll("12345"))
        assertTrue(constraint.matchesAll("123456"))
    }

    @Test
    fun ifExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItSucceeds() {

        val constraint = ScopedConstraintBuilder<Model>().apply {
            on(Model::n) ifExists {
                max(5)
            }
        }

        assertTrue(constraint.matchesAll(Model(null)))
        assertTrue(constraint.matchesAll(Model(3)))
        assertFalse(constraint.matchesAll(Model(6)))
    }

    @Test
    fun mustExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItFails() {

        val constraint = ScopedConstraintBuilder<Model>().apply {
            on(Model::n) mustExist {
                max(5)
            }
        }

        assertFalse(constraint.matchesAll(Model(null)))
        assertTrue(constraint.matchesAll(Model(3)))
        assertFalse(constraint.matchesAll(Model(6)))
    }

    @Test
    fun validateAll_returnsTrueIfTheItemMatchesAllTheValidations() {

        val constraint = ScopedConstraintBuilder<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(constraint.matchesAll(-2))
        assertTrue(constraint.matchesAll(0))
        assertTrue(constraint.matchesAll(2))
        assertFalse(constraint.matchesAll(1))
        assertFalse(constraint.matchesAll(3))
        assertFalse(constraint.matchesAll(4))
        assertFalse(constraint.matchesAll(5))
        assertFalse(constraint.matchesAll(6))
    }

    @Test
    fun validateAny_returnsTrueIfTheItemMatchesAnyOfTheValidations() {

        val constraint = ScopedConstraintBuilder<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(constraint.matchesAny(-2))
        assertTrue(constraint.matchesAny(-1))
        assertTrue(constraint.matchesAny(0))
        assertTrue(constraint.matchesAny(1))
        assertTrue(constraint.matchesAny(2))
        assertTrue(constraint.matchesAny(3))
        assertTrue(constraint.matchesAny(4))
        assertTrue(constraint.matchesAny(6))
        assertFalse(constraint.matchesAny(5))
        assertFalse(constraint.matchesAny(7))
        assertFalse(constraint.matchesAny(9))
    }

    @Test
    fun validateNone_returnsTrueIfTheItemMatchesNoneOfTheValidations() {

        val constraint = ScopedConstraintBuilder<Int>().apply {
            max(3)
            isEven()
        }

        assertTrue(constraint.matchesNone(5))
        assertTrue(constraint.matchesNone(7))
        assertTrue(constraint.matchesNone(9))
        assertFalse(constraint.matchesNone(-2))
        assertFalse(constraint.matchesNone(-1))
        assertFalse(constraint.matchesNone(0))
        assertFalse(constraint.matchesNone(1))
        assertFalse(constraint.matchesNone(2))
        assertFalse(constraint.matchesNone(3))
        assertFalse(constraint.matchesNone(4))
        assertFalse(constraint.matchesNone(6))
    }
}
