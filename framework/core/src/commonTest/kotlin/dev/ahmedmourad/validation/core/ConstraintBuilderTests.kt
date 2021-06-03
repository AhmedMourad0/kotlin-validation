package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.max
import kotlin.test.Test
import kotlin.test.*

@InternalValidationApi
class ConstraintBuilderTests {

    private data class Model(val n: Int?)

    @Test
    fun validation_addsDirectValidationToThisConstraint() {

        val expected = Constraint<Boolean>(
            "SomeConstraint",
            emptyList(),
            listOf(Validation { it }),
            emptyList()
        )

        val actual = ConstraintBuilder<Boolean>(expected.violation).apply {
            validation(expected.validations.first()::validate)
        }.build()

        assertTrue(actual.validations.all { it.validate(true) })
        assertFalse(actual.validations.all { it.validate(false) })
    }

    @Test
    fun meta_addsMetadataToThisConstraint() {

        val expectedMeta = Metadata<Int, Int>("someMeta") { 4 }

        val expected = Constraint(
            "SomeConstraint",
            emptyList(),
            emptyList(),
            listOf(expectedMeta)
        )

        val actual = ConstraintBuilder<Int>(expected.violation).apply {
            meta(expectedMeta.name, expectedMeta::get)
        }.build()

        val actualMeta = actual.metadata.first()

        assertEquals(expectedMeta.name, actualMeta.name)
        assertEquals(expectedMeta.get(5), actualMeta.get(5))
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun include_addsIncludedConstraintToThisConstraint() {

        val intConstrainer = object : Constrains<Int> {
            override val constraints by describe {
                constraint("TooHigh") {
                    max(5)
                }
            }
        }

        val expectedIncludedConstraint = IncludedConstraints<Model, Int, Constrains<Int>>(
            "someMeta",
            Model::n
        ) { _, _ -> intConstrainer }

        val expected = Constraint(
            "SomeConstraint",
            listOf(expectedIncludedConstraint),
            emptyList(),
            emptyList()
        )

        val actual = ConstraintBuilder<Model>(expected.violation).apply {
            include(
                expectedIncludedConstraint.meta,
                expectedIncludedConstraint::getConstrained,
                expectedIncludedConstraint::getConstrainer
            )
        }.build()

        val actualIncludedConstraint = actual.includedConstraints.first() as IncludedConstraints<Model, Int, Constrains<Int>>

        assertEquals(expectedIncludedConstraint.meta, actualIncludedConstraint.meta)
        assertEquals(
            expectedIncludedConstraint.getConstrained(Model(5)),
            actualIncludedConstraint.getConstrained(Model(5))
        )
        assertEquals(
            expectedIncludedConstraint.getConstrainer(Model(5), 5),
            actualIncludedConstraint.getConstrainer(Model(5), 5)
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun include1_addsIncludedConstraintToThisConstraint() {

        val intConstrainer = object : Constrains<Int> {
            override val constraints by describe {
                constraint("TooHigh") {
                    max(5)
                }
            }
        }

        val expectedIncludedConstraint = IncludedConstraints<Int, Int, Constrains<Int>>(
            "someMeta",
            { 5 },
            { _, _ -> intConstrainer }
        )

        val expected = Constraint(
            "SomeConstraint",
            listOf(expectedIncludedConstraint),
            emptyList(),
            emptyList()
        )

        val actual = ConstraintBuilder<Int>(expected.violation).apply {
            include(
                expectedIncludedConstraint.meta,
                expectedIncludedConstraint::getConstrained,
                expectedIncludedConstraint::getConstrainer
            )
        }.build()

        val actualIncludedConstraint = actual.includedConstraints.first() as IncludedConstraints<Int, Int, Constrains<Int>>

        assertEquals(expectedIncludedConstraint.meta, actualIncludedConstraint.meta)
        assertEquals(
            expectedIncludedConstraint.getConstrained(5),
            actualIncludedConstraint.getConstrained(5)
        )
        assertEquals(
            expectedIncludedConstraint.getConstrainer(5, 5),
            actualIncludedConstraint.getConstrainer(5, 5)
        )
    }

    @Test
    fun include2_addsIncludedConstraintToThisConstraint() {

        val intConstrainer = object : Constrains<Int> {
            override val constraints by describe { }
        }

        val model = Model(5)

        val constraint = ConstraintBuilder<Int>("SomeConstraint").apply {
            include(
                "someMeta",
                model::n,
                { _, _ -> intConstrainer }
            )
        }.build()

        assertTrue(constraint.includedConstraints.all { ic -> ic.isValid(7) { (it as Int) < 6 } })
        assertFalse(constraint.includedConstraints.all { ic -> ic.isValid(7) { (it as Int) > 6 } })
    }

    @Test
    fun on_changesTheValidatorScopeFromTheConstrainedTypeToTheGivenItem() {

        val constraint = ConstraintBuilder<String>("SomeConstraint").apply {
            on(String::length) {
                max(5)
            }
        }.build()

        assertTrue(constraint.validations.all { it.validate("123") })
        assertFalse(constraint.validations.all { it.validate("123456") })
    }

    @Test
    fun on1_changesTheValidatorScopeFromTheConstrainedTypeToTheGivenItem() {

        val constraint = ConstraintBuilder<String>("SomeConstraint").apply {
            on({ it.length }) {
                max(5)
            }
        }.build()

        assertTrue(constraint.validations.all { it.validate("123") })
        assertFalse(constraint.validations.all { it.validate("123456") })
    }

    @Test
    fun on2_changesTheValidatorScopeFromTheConstrainedTypeToTheGivenItem() {

        val constraint = ConstraintBuilder<String>("SomeConstraint").apply {
            on("123"::length) {
                max(5)
            }
        }.build()

        assertTrue(constraint.validations.all { it.validate("123") })
        assertTrue(constraint.validations.all { it.validate("123456") })
    }

    @Test
    fun ifExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItSucceeds() {

        val constraint = ConstraintBuilder<Model>("SomeConstraint").apply {
            on(Model::n) ifExists {
                max(5)
            }
        }.build()

        assertTrue(constraint.validations.all { it.validate(Model(null)) })
        assertTrue(constraint.validations.all { it.validate(Model(3)) })
        assertFalse(constraint.validations.all { it.validate(Model(6)) })
    }

    @Test
    fun mustExists_appliesTheValidationsIfTheValueIsNotNullOtherwiseItFails() {

        val constraint = ConstraintBuilder<Model>("SomeConstraint").apply {
            on(Model::n) mustExist {
                max(5)
            }
        }.build()

        assertFalse(constraint.validations.all { it.validate(Model(null)) })
        assertTrue(constraint.validations.all { it.validate(Model(3)) })
        assertFalse(constraint.validations.all { it.validate(Model(6)) })
    }
}
