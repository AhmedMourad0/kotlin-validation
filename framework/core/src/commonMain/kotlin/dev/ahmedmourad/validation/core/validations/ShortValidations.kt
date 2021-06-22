package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Short>.isDivisibleBy(
    crossinline other: (Short) -> Short
) = validation {
    it % other(it) == 0
}

fun Constraint<Short>.isDivisibleBy(other: Short) = isDivisibleBy { other }

inline fun Constraint<Short>.isNotDivisibleBy(
    crossinline other: (Short) -> Short
) = validation {
    it % other(it) != 0
}

fun Constraint<Short>.isNotDivisibleBy(other: Short) = isNotDivisibleBy { other }

fun Constraint<Short>.isEven() = validation {
    it % 2 == 0
}

fun Constraint<Short>.isOdd() = validation {
    it % 2 != 0
}

fun Constraint<Short>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Constraint<Short>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Constraint<Short>.isZero() = validation {
    it == 0.toShort()
}

fun Constraint<Short>.isNotZero() = validation {
    it != 0.toShort()
}

fun Constraint<Short>.isPrime() = validation {
    if (it < 2) return@validation false
    (2..(it / 2)).none { n ->
        it % n == 0
    }
}

fun Constraint<Short>.isNotPrime() = validation {
    if (it < 2) return@validation true
    (2..(it / 2)).any { n ->
        it % n == 0
    }
}
