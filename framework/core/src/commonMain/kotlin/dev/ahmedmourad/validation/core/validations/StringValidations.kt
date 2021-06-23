package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

fun Constraint<String>.isInteger() = validation {
    subject.toLongOrNull() != null
}

fun Constraint<String>.isNumber() = validation {
    subject.toDoubleOrNull() != null
}

fun Constraint<String>.isPositiveInteger(orZero: Boolean) = validation {
    if (orZero) {
        subject.toLongOrNull()?.let { it >= 0 } ?: false
    } else {
        subject.toLongOrNull()?.let { it > 0 } ?: false
    }
}

fun Constraint<String>.isNegativeInteger(orZero: Boolean) = validation {
    if (orZero) {
        subject.toLongOrNull()?.let { it <= 0 } ?: false
    } else {
        subject.toLongOrNull()?.let { it < 0 } ?: false
    }
}

fun Constraint<String>.isPositiveNumber(orZero: Boolean) = validation {
    if (orZero) {
        subject.toDoubleOrNull()?.let { it >= 0.0 } ?: false
    } else {
        subject.toDoubleOrNull()?.let { it > 0.0 } ?: false
    }
}

fun Constraint<String>.isNegativeNumber(orZero: Boolean) = validation {
    if (orZero) {
        subject.toDoubleOrNull()?.let { it <= 0.0 } ?: false
    } else {
        subject.toDoubleOrNull()?.let { it < 0.0 } ?: false
    }
}

fun Constraint<String>.isZero() = validation {
    (subject.toDoubleOrNull() ?: 1.0) == 0.0
}

fun Constraint<String>.isNotZero() = validation {
    (subject.toDoubleOrNull() ?: 1.0) != 0.0
}

inline fun Constraint<String>.isEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    subject.equals(other(subject), ignoreCase)
}

fun Constraint<String>.isEqualTo(
    other: String,
    ignoreCase: Boolean
) = isEqualTo(ignoreCase) { other }

inline fun Constraint<String>.isNotEqualTo(
    ignoreCase: Boolean,
    crossinline other: (String) -> String
) = validation {
    !subject.equals(other(subject), ignoreCase)
}

fun Constraint<String>.isNotEqualTo(
    other: String,
    ignoreCase: Boolean
) = isNotEqualTo(ignoreCase) { other }
