package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Long>.isDivisibleBy(
    crossinline other: (Long) -> Long
) = validation {
    subject % other(subject) == 0L
}

fun Constraint<Long>.isDivisibleBy(other: Long) = isDivisibleBy { other }

inline fun Constraint<Long>.isNotDivisibleBy(
    crossinline other: (Long) -> Long
) = validation {
    subject % other(subject) != 0L
}

fun Constraint<Long>.isNotDivisibleBy(other: Long) = isNotDivisibleBy { other }

fun Constraint<Long>.isEven() = validation {
    subject % 2 == 0L
}

fun Constraint<Long>.isOdd() = validation {
    subject % 2 != 0L
}

fun Constraint<Long>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        subject >= 0
    } else {
        subject > 0
    }
}

fun Constraint<Long>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        subject <= 0
    } else {
        subject < 0
    }
}

fun Constraint<Long>.isZero() = validation {
    subject == 0L
}

fun Constraint<Long>.isNotZero() = validation {
    subject != 0L
}

fun Constraint<Long>.isPrime() = validation {
    if (subject < 2) return@validation false
    (2..(subject / 2)).none { n ->
        subject % n == 0L
    }
}

fun Constraint<Long>.isNotPrime() = validation {
    if (subject < 2) return@validation true
    (2..(subject / 2)).any { n ->
        subject % n == 0L
    }
}
