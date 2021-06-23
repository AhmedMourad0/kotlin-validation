package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Int>.isDivisibleBy(
    crossinline other: (Int) -> Int
) = validation {
    subject % other(subject) == 0
}

fun Constraint<Int>.isDivisibleBy(other: Int) = isDivisibleBy { other }

inline fun Constraint<Int>.isNotDivisibleBy(
    crossinline other: (Int) -> Int
) = validation {
    subject % other(subject) != 0
}

fun Constraint<Int>.isNotDivisibleBy(other: Int) = isNotDivisibleBy { other }

fun Constraint<Int>.isEven() = validation {
    subject % 2 == 0
}

fun Constraint<Int>.isOdd() = validation {
    subject % 2 != 0
}

fun Constraint<Int>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        subject >= 0
    } else {
        subject > 0
    }
}

fun Constraint<Int>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        subject <= 0
    } else {
        subject < 0
    }
}

fun Constraint<Int>.isZero() = validation {
    subject == 0
}

fun Constraint<Int>.isNotZero() = validation {
    subject != 0
}

fun Constraint<Int>.isPrime() = validation {
    if (subject < 2) return@validation false
    (2..(subject / 2)).none { n ->
        subject % n == 0
    }
}

fun Constraint<Int>.isNotPrime() = validation {
    if (subject < 2) return@validation true
    (2..(subject / 2)).any { n ->
        subject % n == 0
    }
}
