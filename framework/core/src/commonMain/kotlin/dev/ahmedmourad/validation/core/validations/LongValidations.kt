package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Long>.isDivisibleBy(
    crossinline other: (Long) -> Long
) = validation {
    it % other(it) == 0L
}

fun Constraint<Long>.isDivisibleBy(other: Long) = isDivisibleBy { other }

inline fun Constraint<Long>.isNotDivisibleBy(
    crossinline other: (Long) -> Long
) = validation {
    it % other(it) != 0L
}

fun Constraint<Long>.isNotDivisibleBy(other: Long) = isNotDivisibleBy { other }

fun Constraint<Long>.isEven() = validation {
    it % 2 == 0L
}

fun Constraint<Long>.isOdd() = validation {
    it % 2 != 0L
}

fun Constraint<Long>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Constraint<Long>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Constraint<Long>.isZero() = validation {
    it == 0L
}

fun Constraint<Long>.isNotZero() = validation {
    it != 0L
}

fun Constraint<Long>.isPrime() = validation {
    if (it < 2) return@validation false
    (2..(it / 2)).none { n ->
        it % n == 0L
    }
}

fun Constraint<Long>.isNotPrime() = validation {
    if (it < 2) return@validation true
    (2..(it / 2)).any { n ->
        it % n == 0L
    }
}
