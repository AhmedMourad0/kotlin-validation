package dev.ahmedmourad.validation.core

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstraintsBuilderTests : Validator<Int> {

    override val constraints get() = TODO()

    @Test
    fun describe_createsConstraintsDescriptorWithTheDeclaredConstraints() {

        assertEquals(ValidatorDescriptor<Int>(emptyList()), describe { }.value)

        val expectedConstraint =  ConstraintDescriptor<Int>(
            "SomeViolation",
            emptyList(),
            emptyList(),
            emptyList()
        )
        val expected = ValidatorDescriptor(listOf(expectedConstraint))

        val actual = describe {
            constraint(expectedConstraint.violation) { }
        }.value

        assertEquals(expected, actual)
    }

    @Test
    fun constraint_declaresConstraintAsPartOfThisConstraintsDescriptor() {

        val expectedMeta = MetadataDescriptor<Int, Int>("someMeta") { 2 }

        val expected = ConstraintDescriptor(
            "SomeViolation",
            emptyList(),
            emptyList(),
            listOf(expectedMeta)
        )

        val actual = ConstraintsBuilder<Int>().apply {
            constraint(expected.violation) {
                meta(expectedMeta.name, expectedMeta::get)
            }
        }.build().first()

        val actualMeta = actual.metadata.first()

        assertEquals(expectedMeta.name, actualMeta.name)
        assertEquals(expectedMeta.get(5), actualMeta.get(5))
    }
}
