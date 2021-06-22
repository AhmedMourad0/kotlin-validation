package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Float>.isDivisibleBy(
    crossinline other: (Float) -> Float
) = validation {
    it % other(it) == 0.0f
}

fun Constraint<Float>.isDivisibleBy(other: Float) = isDivisibleBy { other }

inline fun Constraint<Float>.isNotDivisibleBy(
    crossinline other: (Float) -> Float
) = validation {
    it % other(it) != 0.0f
}

fun Constraint<Float>.isNotDivisibleBy(other: Float) = isNotDivisibleBy { other }

fun Constraint<Float>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0.0f
    } else {
        it > 0.0f
    }
}

fun Constraint<Float>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0.0f
    } else {
        it < 0.0f
    }
}

fun Constraint<Float>.isZero() = validation {
    it == 0.0f
}

fun Constraint<Float>.isNotZero() = validation {
    it != 0.0f
}

fun Constraint<Float>.isNaN() = validation {
    it.isNaN()
}

fun Constraint<Float>.isNotNaN() = validation {
    !it.isNaN()
}

fun Constraint<Float>.isInfinite() = validation {
    it.isInfinite()
}

fun Constraint<Float>.isFinite() = validation {
    it.isFinite()
}

