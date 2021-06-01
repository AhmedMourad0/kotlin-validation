package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator

inline fun Validator<Float>.isDivisibleBy(
    crossinline other: (Float) -> Float
) = validation {
    it % other(it) == 0.0f
}

fun Validator<Float>.isDivisibleBy(other: Float) = isDivisibleBy { other }

inline fun Validator<Float>.isNotDivisibleBy(
    crossinline other: (Float) -> Float
) = validation {
    it % other(it) != 0.0f
}

fun Validator<Float>.isNotDivisibleBy(other: Float) = isNotDivisibleBy { other }

fun Validator<Float>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0.0f
    } else {
        it > 0.0f
    }
}

fun Validator<Float>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0.0f
    } else {
        it < 0.0f
    }
}

fun Validator<Float>.isZero() = validation {
    it == 0.0f
}

fun Validator<Float>.isNotZero() = validation {
    it != 0.0f
}

fun Validator<Float>.isNaN() = validation {
    it.isNaN()
}

fun Validator<Float>.isNotNaN() = validation {
    !it.isNaN()
}

fun Validator<Float>.isInfinite() = validation {
    it.isInfinite()
}

fun Validator<Float>.isFinite() = validation {
    it.isFinite()
}

