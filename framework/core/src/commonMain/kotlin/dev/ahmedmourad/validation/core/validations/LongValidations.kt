package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator

inline fun Validator<Long>.isDivisibleBy(
    crossinline other: (Long) -> Long
) = validation {
    it % other(it) == 0L
}

fun Validator<Long>.isDivisibleBy(other: Long) = isDivisibleBy { other }

inline fun Validator<Long>.isNotDivisibleBy(
    crossinline other: (Long) -> Long
) = validation {
    it % other(it) != 0L
}

fun Validator<Long>.isNotDivisibleBy(other: Long) = isNotDivisibleBy { other }

fun Validator<Long>.isEven() = validation {
    it % 2 == 0L
}

fun Validator<Long>.isOdd() = validation {
    it % 2 != 0L
}

fun Validator<Long>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Validator<Long>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Validator<Long>.isZero() = validation {
    it == 0L
}

fun Validator<Long>.isNotZero() = validation {
    it != 0L
}

fun Validator<Long>.isPrime() = validation {
    if (it < 2) return@validation false
    (2..(it / 2)).none { n ->
        it % n == 0L
    }
}

fun Validator<Long>.isNotPrime() = validation {
    if (it < 2) return@validation true
    (2..(it / 2)).any { n ->
        it % n == 0L
    }
}
