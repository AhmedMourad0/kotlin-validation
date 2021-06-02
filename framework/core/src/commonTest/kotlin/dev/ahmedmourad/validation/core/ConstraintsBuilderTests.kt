package dev.ahmedmourad.validation.core

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstraintsBuilderTests : Constrains<Int> {

    override val constraints get() = TODO()

    @Test
    fun describe_createsConstraintsDescriptorWithTheDeclaredConstraints() {

        assertEquals(ConstraintsDescriptor<Int>(emptyList()), describe { }.value)

        val expectedConstraint =  Constraint<Int>(
            "SomeViolation",
            emptyList(),
            emptyList(),
            emptyList()
        )
        val expected = ConstraintsDescriptor(listOf(expectedConstraint))

        val actual = describe {
            constraint(expectedConstraint.violation) { }
        }.value

        assertEquals(expected, actual)
    }

    @Test
    fun constraint_declaresConstraintAsPartOfThisConstraintsDescriptor() {

        val expectedParam = Parameter<Int, Int>("someParam") { 2 }

        val expected = Constraint(
            "SomeViolation",
            emptyList(),
            emptyList(),
            listOf(expectedParam)
        )

        val actual = ConstraintsBuilder<Int>().apply {
            constraint(expected.violation) {
                param(expectedParam.name, expectedParam::get)
            }
        }.build().first()

        val actualParam = actual.params.first()

        assertEquals(expectedParam.name, actualParam.name)
        assertEquals(expectedParam.get(5), actualParam.get(5))
    }
}
