package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Double>.isDivisibleBy(
    crossinline other: (Double) -> Double
) = validation {
    subject % other(subject) == 0.0
}

fun Constraint<Double>.isDivisibleBy(other: Double) = isDivisibleBy { other }

inline fun Constraint<Double>.isNotDivisibleBy(
    crossinline other: (Double) -> Double
) = validation {
    subject % other(subject) != 0.0
}

fun Constraint<Double>.isNotDivisibleBy(other: Double) = isNotDivisibleBy { other }

fun Constraint<Double>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        subject >= 0.0
    } else {
        subject > 0.0
    }
}

fun Constraint<Double>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        subject <= 0.0
    } else {
        subject < 0.0
    }
}

fun Constraint<Double>.isZero() = validation {
    subject == 0.0
}

fun Constraint<Double>.isNotZero() = validation {
    subject != 0.0
}

fun Constraint<Double>.isNaN() = validation {
    subject.isNaN()
}

fun Constraint<Double>.isNotNaN() = validation {
    !subject.isNaN()
}

fun Constraint<Double>.isInfinite() = validation {
    subject.isInfinite()
}

fun Constraint<Double>.isFinite() = validation {
    subject.isFinite()
}
