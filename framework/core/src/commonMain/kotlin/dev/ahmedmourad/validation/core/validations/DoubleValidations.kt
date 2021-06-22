package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Double>.isDivisibleBy(
    crossinline other: (Double) -> Double
) = validation {
    it % other(it) == 0.0
}

fun Constraint<Double>.isDivisibleBy(other: Double) = isDivisibleBy { other }

inline fun Constraint<Double>.isNotDivisibleBy(
    crossinline other: (Double) -> Double
) = validation {
    it % other(it) != 0.0
}

fun Constraint<Double>.isNotDivisibleBy(other: Double) = isNotDivisibleBy { other }

fun Constraint<Double>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0.0
    } else {
        it > 0.0
    }
}

fun Constraint<Double>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0.0
    } else {
        it < 0.0
    }
}

fun Constraint<Double>.isZero() = validation {
    it == 0.0
}

fun Constraint<Double>.isNotZero() = validation {
    it != 0.0
}

fun Constraint<Double>.isNaN() = validation {
    it.isNaN()
}

fun Constraint<Double>.isNotNaN() = validation {
    !it.isNaN()
}

fun Constraint<Double>.isInfinite() = validation {
    it.isInfinite()
}

fun Constraint<Double>.isFinite() = validation {
    it.isFinite()
}
