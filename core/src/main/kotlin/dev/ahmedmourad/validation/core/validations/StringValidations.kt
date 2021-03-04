package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

fun Validator<String>.isNumber() = validation {
    it.toLongOrNull() != null
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

fun Validator<String>.isPositiveDecimal(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it >= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it > 0.0 } ?: false
    }
}

fun Validator<String>.isNegativeDecimal(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it <= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it < 0.0 } ?: false
    }
}

fun Validator<String>.isZero() = validation {
    (it.toIntOrNull() ?: 1) == 0
}

fun Validator<String>.isNotZero() = validation {
    (it.toLongOrNull() ?: 0L) != 0L
}

inline fun Validator<String>.isEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    it.equals(other(it), ignoreCase)
}

fun Validator<String>.isEqualTo(
    ignoreCase: Boolean,
    other: String
) = isEqualTo(ignoreCase) { other }

inline fun Validator<String>.isNotEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    !it.equals(other(it), ignoreCase)
}

fun Validator<String>.isNotEqualTo(
    ignoreCase: Boolean,
    other: String
) = isNotEqualTo(ignoreCase) { other }
