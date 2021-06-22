package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

fun Constraint<String>.isInteger() = validation {
    it.toLongOrNull() != null
}

fun Constraint<String>.isNumber() = validation {
    it.toDoubleOrNull() != null
}

fun Constraint<String>.isPositiveInteger(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toLongOrNull()?.let { it >= 0 } ?: false
    } else {
        validated.toLongOrNull()?.let { it > 0 } ?: false
    }
}

fun Constraint<String>.isNegativeInteger(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toLongOrNull()?.let { it <= 0 } ?: false
    } else {
        validated.toLongOrNull()?.let { it < 0 } ?: false
    }
}

fun Constraint<String>.isPositiveNumber(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it >= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it > 0.0 } ?: false
    }
}

fun Constraint<String>.isNegativeNumber(orZero: Boolean) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it <= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it < 0.0 } ?: false
    }
}

fun Constraint<String>.isZero() = validation {
    (it.toDoubleOrNull() ?: 1.0) == 0.0
}

fun Constraint<String>.isNotZero() = validation {
    (it.toDoubleOrNull() ?: 1.0) != 0.0
}

inline fun Constraint<String>.isEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    it.equals(other(it), ignoreCase)
}

fun Constraint<String>.isEqualTo(
    other: String,
    ignoreCase: Boolean
) = isEqualTo(ignoreCase) { other }

inline fun Constraint<String>.isNotEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    !it.equals(other(it), ignoreCase)
}

fun Constraint<String>.isNotEqualTo(
    other: String,
    ignoreCase: Boolean
) = isNotEqualTo(ignoreCase) { other }
