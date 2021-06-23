package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Short>.isDivisibleBy(
    crossinline other: (Short) -> Short
) = validation {
    subject % other(subject) == 0
}

fun Constraint<Short>.isDivisibleBy(other: Short) = isDivisibleBy { other }

inline fun Constraint<Short>.isNotDivisibleBy(
    crossinline other: (Short) -> Short
) = validation {
    subject % other(subject) != 0
}

fun Constraint<Short>.isNotDivisibleBy(other: Short) = isNotDivisibleBy { other }

fun Constraint<Short>.isEven() = validation {
    subject % 2 == 0
}

fun Constraint<Short>.isOdd() = validation {
    subject % 2 != 0
}

fun Constraint<Short>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        subject >= 0
    } else {
        subject > 0
    }
}

fun Constraint<Short>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        subject <= 0
    } else {
        subject < 0
    }
}

fun Constraint<Short>.isZero() = validation {
    subject == 0.toShort()
}

fun Constraint<Short>.isNotZero() = validation {
    subject != 0.toShort()
}

fun Constraint<Short>.isPrime() = validation {
    if (subject < 2) return@validation false
    (2..(subject / 2)).none { n ->
        subject % n == 0
    }
}

fun Constraint<Short>.isNotPrime() = validation {
    if (subject < 2) return@validation true
    (2..(subject / 2)).any { n ->
        subject % n == 0
    }
}
