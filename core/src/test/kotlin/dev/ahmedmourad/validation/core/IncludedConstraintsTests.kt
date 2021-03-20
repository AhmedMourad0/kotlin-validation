package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.with
import dev.ahmedmourad.validation.core.validations.*
import org.junit.Before
import org.junit.Test

private const val TOO_SHORT = "TooShort"
private const val EXTREMELY_SHORT = "ExtremelyShort"
private const val TOO_LONG = "TooLong"

@InternalValidationApi
class IncludedConstraintsTests {

    object IntConstrainer : Constrains<Int> {
        override val constraints by describe {
            constraint(violation = TOO_SHORT) {
                min(3)
            }
            constraint(violation = EXTREMELY_SHORT) {
                min(2)
            }
            constraint(violation = TOO_LONG) {
                max(7)
            }
        }
    }

    lateinit var includedConstraints: IncludedConstraints<String, Int, Constrains<Int>>

    @Before
    fun setup() {
        includedConstraints = IncludedConstraints("lengthViolations", String::length) { _, _ ->
            IntConstrainer
        }
    }

    @Test
    fun isValid_checksIfTheGivenConstraintsMatchTheItemGivenTheIsValidExtensionFunctionForTheConstrainer() {
        includedConstraints.with { item: String ->
            isValid(item) { this.findViolatedConstraints(it).isEmpty() }
        }.allMatch(
            "123",
            "1234",
            "12345",
            "123456",
            "1234567"
        ).allFail(
            "",
            "1",
            "12",
            "12345678",
            "123456789"
        )
    }

    @Test
    fun findViolations_checksIfTheGivenConstraintsMatchTheItemGivenTheIsValidExtensionFunctionForTheConstrainer() {
        includedConstraints.with { item: Pair<String, List<String>> ->
            findViolations(item.first) {
                this.findViolatedConstraints(it).map(Constraint<Int>::violation).illegal()
            }.contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = true,
                other = item.second
            )
        }.allMatch(
            "123" to emptyList(),
            "1234" to emptyList(),
            "12345" to emptyList(),
            "123456" to emptyList(),
            "1234567" to emptyList(),
            "" to listOf(TOO_SHORT, EXTREMELY_SHORT),
            "1" to listOf(TOO_SHORT, EXTREMELY_SHORT),
            "12" to listOf(TOO_SHORT),
            "12345678" to listOf(TOO_LONG),
            "123456789" to listOf(TOO_LONG)
        ).allFail(
            "123" to listOf(TOO_SHORT),
            "1234" to listOf(TOO_LONG),
            "12345" to listOf(TOO_SHORT),
            "123456" to listOf(TOO_LONG),
            "1234567" to listOf(TOO_SHORT),
            "" to emptyList(),
            "1" to emptyList(),
            "12" to listOf(EXTREMELY_SHORT),
            "12345678" to emptyList(),
            "123456789" to emptyList()
        )
    }

    private fun <T : Any> Constrains<T>.findViolatedConstraints(item: T): List<Constraint<T>> {
        return this.constraints.filterNot { constraint ->
            constraint.validations.all { validation ->
                validation.validate(item)
            }
        }
    }
}
