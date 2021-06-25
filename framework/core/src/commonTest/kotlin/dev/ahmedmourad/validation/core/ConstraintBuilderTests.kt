package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.max
import kotlin.test.Test
import kotlin.test.*

@InternalValidationApi
class ConstraintBuilderTests {

    private data class Model(val n: Int?)

    @Test
    fun validation_addsDirectValidationToThisConstraint() {

        val expected = ConstraintDescriptor<Boolean>(
            "SomeConstraint",
            emptyList(),
            listOf(ValidationDescriptor { subject }),
            emptyList()
        )

        val actual = ConstraintBuilder<Boolean>(expected.violation).apply {
            validation { expected.validations.first().validate(subject) }
        }.build()

        assertTrue(actual.validations.all { it.validate(true) })
        assertFalse(actual.validations.all { it.validate(false) })
    }

    @Test
    fun meta_addsMetadataToThisConstraint() {

        val expectedMeta = MetadataDescriptor<Int, Int>("someMeta") { 4 }

        val expected = ConstraintDescriptor(
            "SomeConstraint",
            emptyList(),
            emptyList(),
            listOf(expectedMeta)
        )

        val actual = ConstraintBuilder<Int>(expected.violation).apply {
            meta(expectedMeta.name) { expectedMeta.get(subject) }
        }.build()

        val actualMeta = actual.metadata.first()

        assertEquals(expectedMeta.name, actualMeta.name)
        assertEquals(expectedMeta.get(5), actualMeta.get(5))
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun include_addsIncludedValidatorToThisConstraint() {

        val intValidator = object : Validator<Int> {
            override val constraints by describe {
                constraint("TooHigh") {
                    max(5)
                }
            }
        }

        val expectedIncludedValidator = IncludedValidatorDescriptor<Model, Int, Validator<Int>>(
            "someMeta"
        ) { subject.n to intValidator }

        val expected = ConstraintDescriptor(
            "SomeConstraint",
            listOf(expectedIncludedValidator),
            emptyList(),
            emptyList()
        )

        val actual = ConstraintBuilder<Model>(expected.violation).apply {
            include(expectedIncludedValidator.meta) {
                expectedIncludedValidator.getBinding(subject)
            }
        }.build()

        val actualIncludedValidator = actual.includedValidators.first() as IncludedValidatorDescriptor<Model, Int, Validator<Int>>

        assertEquals(expectedIncludedValidator.meta, actualIncludedValidator.meta)
        assertEquals(
            expectedIncludedValidator.getBinding(Model(5)),
            actualIncludedValidator.getBinding(Model(5))
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun include1_addsIncludedValidatorToThisConstraint() {

        val intValidator = object : Validator<Int> {
            override val constraints by describe {
                constraint("TooHigh") {
                    max(5)
                }
            }
        }

        val expectedIncludedValidator = IncludedValidatorDescriptor<Int, Int, Validator<Int>>(
            "someMeta"
        ) { 5 to intValidator }

        val expected = ConstraintDescriptor(
            "SomeConstraint",
            listOf(expectedIncludedValidator),
            emptyList(),
            emptyList()
        )

        val actual = ConstraintBuilder<Int>(expected.violation).apply {
            include(expectedIncludedValidator.meta) {
                expectedIncludedValidator.getBinding(subject)
            }
        }.build()

        val actualIncludedValidator = actual.includedValidators.first() as IncludedValidatorDescriptor<Int, Int, Validator<Int>>

        assertEquals(expectedIncludedValidator.meta, actualIncludedValidator.meta)
        assertEquals(
            expectedIncludedValidator.getBinding(5),
            actualIncludedValidator.getBinding(5)
        )
    }

    @Test
    fun include2_addsIncludedValidatorToThisConstraint() {

        val intValidator = object : Validator<Int> {
            override val constraints by describe { }
        }

        val model = Model(5)

        val constraint = ConstraintBuilder<Int>("SomeConstraint").apply {
            include("someMeta") {
                model::n to intValidator
            }

        }.build()

        assertTrue(constraint.includedValidators.all { ic -> ic.isValid(7) { (it as Int) < 6 } })
        assertFalse(constraint.includedValidators.all { ic -> ic.isValid(7) { (it as Int) > 6 } })
    }

    @Test
    fun on_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ConstraintBuilder<String>("SomeConstraint").apply {
            on(String::length) {
                max(5)
            }
        }.build()

        assertTrue(constraint.validations.all { it.validate("123") })
        assertFalse(constraint.validations.all { it.validate("123456") })
    }

    @Test
    fun on1_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

        val constraint = ConstraintBuilder<String>("SomeConstraint").apply {
            on({ subject.length }) {
                max(5)
            }
        }.build()

        assertTrue(constraint.validations.all { it.validate("123") })
        assertFalse(constraint.validations.all { it.validate("123456") })
    }

    @Test
    fun on2_changesTheConstraintScopeFromTheSubjectTypeToTheGivenItem() {

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
