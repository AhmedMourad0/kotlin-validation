package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Float>.isDivisibleBy(
    crossinline other: (Float) -> Float
) = validation {
    subject % other(subject) == 0.0f
}

fun Constraint<Float>.isDivisibleBy(other: Float) = isDivisibleBy { other }

inline fun Constraint<Float>.isNotDivisibleBy(
    crossinline other: (Float) -> Float
) = validation {
    subject % other(subject) != 0.0f
}

fun Constraint<Float>.isNotDivisibleBy(other: Float) = isNotDivisibleBy { other }

fun Constraint<Float>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        subject >= 0.0f
    } else {
        subject > 0.0f
    }
}

fun Constraint<Float>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        subject <= 0.0f
    } else {
        subject < 0.0f
    }
}

fun Constraint<Float>.isZero() = validation {
    subject == 0.0f
}

fun Constraint<Float>.isNotZero() = validation {
    subject != 0.0f
}

fun Constraint<Float>.isNaN() = validation {
    subject.isNaN()
}

fun Constraint<Float>.isNotNaN() = validation {
    !subject.isNaN()
}

fun Constraint<Float>.isInfinite() = validation {
    subject.isInfinite()
}

fun Constraint<Float>.isFinite() = validation {
    subject.isFinite()
}

