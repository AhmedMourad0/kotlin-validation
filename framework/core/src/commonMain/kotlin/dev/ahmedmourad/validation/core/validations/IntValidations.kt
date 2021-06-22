package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Int>.isDivisibleBy(
    crossinline other: (Int) -> Int
) = validation {
    it % other(it) == 0
}

fun Constraint<Int>.isDivisibleBy(other: Int) = isDivisibleBy { other }

inline fun Constraint<Int>.isNotDivisibleBy(
    crossinline other: (Int) -> Int
) = validation {
    it % other(it) != 0
}

fun Constraint<Int>.isNotDivisibleBy(other: Int) = isNotDivisibleBy { other }

fun Constraint<Int>.isEven() = validation {
    it % 2 == 0
}

fun Constraint<Int>.isOdd() = validation {
    it % 2 != 0
}

fun Constraint<Int>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Constraint<Int>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Constraint<Int>.isZero() = validation {
    it == 0
}

fun Constraint<Int>.isNotZero() = validation {
    it != 0
}

fun Constraint<Int>.isPrime() = validation {
    if (it < 2) return@validation false
    (2..(it / 2)).none { n ->
        it % n == 0
    }
}

fun Constraint<Int>.isNotPrime() = validation {
    if (it < 2) return@validation true
    (2..(it / 2)).any { n ->
        it % n == 0
    }
}
