package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator

inline fun Validator<Double>.isDivisibleBy(
    crossinline other: (Double) -> Double
) = validation {
    it % other(it) == 0.0
}

fun Validator<Double>.isDivisibleBy(other: Double) = isDivisibleBy { other }

inline fun Validator<Double>.isNotDivisibleBy(
    crossinline other: (Double) -> Double
) = validation {
    it % other(it) != 0.0
}

fun Validator<Double>.isNotDivisibleBy(other: Double) = isNotDivisibleBy { other }

fun Validator<Double>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0.0
    } else {
        it > 0.0
    }
}

fun Validator<Double>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0.0
    } else {
        it < 0.0
    }
}

fun Validator<Double>.isZero() = validation {
    it == 0.0
}

fun Validator<Double>.isNotZero() = validation {
    it != 0.0
}

fun Validator<Double>.isNaN() = validation {
    it.isNaN()
}

fun Validator<Double>.isNotNaN() = validation {
    !it.isNaN()
}

fun Validator<Double>.isInfinite() = validation {
    it.isInfinite()
}

fun Validator<Double>.isFinite() = validation {
    it.isFinite()
}
