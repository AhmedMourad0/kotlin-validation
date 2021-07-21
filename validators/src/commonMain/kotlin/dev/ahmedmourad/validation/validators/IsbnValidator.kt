package dev.ahmedmourad.validation.validators

import dev.ahmedmourad.validation.core.*
import dev.ahmedmourad.validation.core.validations.*

@ValidatorConfig(subjectAlias = "Isbn")
class IsbnValidator : Validator<String> {
    override val constraints by describe {
        val parts = evaluate { subject.split('-') }
        constraint(violation = "MalformedIsbnCode") {
            meta("actual") { subject.length }
            hasCorrectPartsCount()
            on(parts) {
                forAll {
                    doesNotContainWhiteSpaces()
                    isPositiveInteger(orZero = true)
                }
            }
        }
        constraint(violation = "InvalidEanPrefix") {
            val prefix = evaluate {
                if (parts.get().size == 5) {
                    parts.get().firstOrNull()
                } else {
                    null
                }
            }
            meta("value", prefix)
            on(prefix) ifExists {
                inValues("978", "979")
            }
        }
        constraint(violation = "InvalidRegistrationGroup") {
            val group = evaluate {
                if (parts.get().size == 5) {
                    parts.get().getOrNull(1)
                } else {
                    parts.get().getOrNull(0)
                }
            }
            meta("value", group)
            on(group) ifExists {
                lengthIn(1, 5)
            }
        }
        constraint(violation = "InvalidRegistrant") {
            val registrant = evaluate {
                if (parts.get().size == 5) {
                    parts.get().getOrNull(2)
                } else {
                    parts.get().getOrNull(1)
                }
            }
            meta("value", registrant)
            on(registrant) ifExists {
                lengthIn(1, 7)
            }
        }
        constraint(violation = "InvalidPublication") {
            val publication = evaluate {
                if (parts.get().size == 5) {
                    parts.get().getOrNull(3)
                } else {
                    parts.get().getOrNull(2)
                }
            }
            meta("value", publication)
            on(publication) ifExists {
                lengthIn(1, 6)
            }
        }
        constraint(violation = "InvalidCheckDigit") {
            meta("value") { parts.get().lastOrNull() }
            isCheckDigitInRange()
            isCorrectCheckDigit()
        }
    }
}

fun Constraint<String>.hasCorrectPartsCount() = validation {
    val parts = subject.split('-')
    when (subject.length) {
        10 + 3 -> parts.size == 4
        13 + 4 -> parts.size == 5
        else -> false
    }
}

fun Constraint<String>.isCheckDigitInRange() = validation {
    val digit = subject.split('-').last()
    when (subject.length) {
        10 + 3 -> digit.equals("x", true) || digit.toIntOrNull() in 0..9
        13 + 4 -> digit.toIntOrNull() in 0..9
        else -> false
    }
}

fun Constraint<String>.isCorrectCheckDigit() = validation {

    val digits = subject.replace("-", "")
        .map(kotlin.Char::digitToInt)

    if (subject.length == 13) {
        digits.mapIndexed { index, n ->
            n * (10 - index)
        }.sum() % 11
    } else {
        digits.mapIndexed { index, n ->
            n * ((index % 2) * 2 + 1)
        }.sum() % 10
    } == 0
}
