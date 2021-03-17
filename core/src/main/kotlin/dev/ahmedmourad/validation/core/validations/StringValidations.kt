package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

fun Validator<String>.isInteger() = validation {
    it.toLongOrNull() != null
}

fun Validator<String>.isNumber() = validation {
    it.toDoubleOrNull() != null
}

fun Validator<String>.isPositiveInteger(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toLongOrNull()?.let { it >= 0 } ?: false
    } else {
        validated.toLongOrNull()?.let { it > 0 } ?: false
    }
}

fun Validator<String>.isNegativeInteger(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toLongOrNull()?.let { it <= 0 } ?: false
    } else {
        validated.toLongOrNull()?.let { it < 0 } ?: false
    }
}

fun Validator<String>.isPositiveNumber(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it >= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it > 0.0 } ?: false
    }
}

fun Validator<String>.isNegativeNumber(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it <= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it < 0.0 } ?: false
    }
}

fun Validator<String>.isZero() = validation {
    (it.toDoubleOrNull() ?: 1.0) == 0.0
}

fun Validator<String>.isNotZero() = validation {
    (it.toDoubleOrNull() ?: 1.0) != 0.0
}

inline fun Validator<String>.isEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    it.equals(other(it), ignoreCase)
}

fun Validator<String>.isEqualTo(
    other: String,
    ignoreCase: Boolean
) = isEqualTo(ignoreCase) { other }

inline fun Validator<String>.isNotEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    !it.equals(other(it), ignoreCase)
}

fun Validator<String>.isNotEqualTo(
    other: String,
    ignoreCase: Boolean
) = isNotEqualTo(ignoreCase) { other }
